import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

import { OrderDetail, OrderItemRow, OrderPartyBlock } from './sales-order.service';

type Rgb = [number, number, number];

/**
 * Order PDF.
 *
 * snp21 produced this with html2canvas: it rasterised the on-screen block to a PNG and
 * pushed the image into jsPDF. That gives a blurry page with no selectable text, a file
 * several megabytes wide, and a broken page break - its loop re-added the whole image on
 * every page instead of slicing it, so page two onwards repeated page one.
 *
 * This writes the document instead, so the text is real text: selectable, searchable,
 * small, and paginated by jspdf-autotable with the header row repeated. No html2canvas
 * dependency, and the output does not depend on what the screen happened to look like.
 *
 * Laid out as a commercial document rather than a data dump: the brand mark and document
 * number up top, the order's references in one panel, bill-to and ship-to side by side,
 * the lines, a totals box with the amount in words, the terms, and a signature block - the
 * parts a customer or a factory expects to find on an order they are asked to act on.
 */
@Injectable({ providedIn: 'root' })
export class OrderPdfService {
  private static readonly MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  private readonly margin = 14;
  /** The logo's own blue, so the page and the mark agree. */
  private readonly brand: Rgb = [90, 158, 219];
  private readonly brandDark: Rgb = [28, 78, 138];
  private readonly ink: Rgb = [33, 37, 41];
  private readonly muted: Rgb = [108, 117, 125];
  private readonly line: Rgb = [222, 226, 230];
  private readonly panel: Rgb = [245, 248, 252];

  private logo?: Promise<string | null>;

  /**
   * Builds and saves the PDF. Async only because the logo is fetched once and cached; a
   * logo that cannot be loaded falls back to the name in text rather than failing the file.
   */
  async download(order: OrderDetail, kind: 'sales' | 'purchase'): Promise<void> {
    const logo = await this.loadLogo();
    const doc = new jsPDF({ unit: 'mm', format: 'a4', compress: true });
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    const contentWidth = pageWidth - this.margin * 2;
    const title = kind === 'purchase' ? 'PURCHASE ORDER' : 'SALES ORDER';
    const orderNo = order.refNo ?? String(order.id);
    // Guarded although the type says present: an order saved before terms existed arrives without them.
    const termsReq = (order.termsReq as OrderDetail['termsReq'] | null | undefined) ?? {};
    const currency = (termsReq.currency ?? '').trim();

    // ── Masthead ─────────────────────────────────────────────────────────
    const logoHeight = 10;
    if (logo) {
      doc.addImage(logo, 'PNG', this.margin, 12, logoHeight * (1162 / 246), logoHeight);
    } else {
      this.font(doc, 'bold', 18, this.brandDark).text('SCHIEBEL', this.margin, 20);
    }
    this.font(doc, 'bold', 17, this.brandDark).text(title, pageWidth - this.margin, 17, { align: 'right' });
    this.font(doc, 'normal', 9, this.muted).text(`No. ${orderNo}`, pageWidth - this.margin, 22.5, { align: 'right' });

    doc.setDrawColor(...this.brand).setLineWidth(0.8);
    doc.line(this.margin, 27, pageWidth - this.margin, 27);
    doc.setLineWidth(0.2);

    // ── Order references ─────────────────────────────────────────────────
    const refs: [string, string][] = [
      ['Order No', orderNo || '-'],
      ['Order Date', this.date(order.orderDate)],
      ['Project', order.projectName ?? order.name ?? '-'],
      ['Delivery Date', this.date(order.deliveryDate)],
      ['Customer Order Ref', order.customerOrderRef ?? '-'],
      ['Version', order.version == null ? '-' : String(order.version)],
      ['Factory Offer Ref', order.factoryRef ?? '-'],
      ['Status', order.status ?? '-'],
    ];
    let y = 32;
    const refRows = Math.ceil(refs.length / 2);
    const refRowHeight = 9;
    const refHeight = refRows * refRowHeight + 4;
    this.box(doc, this.margin, y, contentWidth, refHeight, this.panel);
    const colWidth = contentWidth / 2;
    refs.forEach(([label, value], i) => {
      const x = this.margin + 5 + (i % 2) * colWidth;
      const rowY = y + 6 + Math.floor(i / 2) * refRowHeight;
      this.font(doc, 'normal', 7, this.muted).text(label.toUpperCase(), x, rowY);
      this.font(doc, 'bold', 9.5, this.ink).text(this.clip(value, 48), x, rowY + 4.2);
    });
    y += refHeight + 6;

    // ── Parties ──────────────────────────────────────────────────────────
    const primary = kind === 'purchase' ? order.supplierDetails : order.customerDetails;
    const primaryLabel = kind === 'purchase' ? 'SUPPLIER' : 'BILL TO';
    const consignee = this.hasParty(order.consigneeDetails) ? order.consigneeDetails : null;
    const partyWidth = (contentWidth - 6) / 2;
    const shipPlaceholder = kind === 'purchase' ? 'As per order' : 'Same as Bill To';
    const leftLines = this.partyLines(doc, primary, '-', partyWidth);
    const rightLines = this.partyLines(doc, consignee, shipPlaceholder, partyWidth);
    // One height for both, so the pair reads as a row rather than two unrelated boxes.
    const cardHeight = 10 + Math.max(leftLines.length, rightLines.length) * 4.6;
    this.partyCard(doc, primaryLabel, leftLines, false, this.margin, y, partyWidth, cardHeight);
    this.partyCard(
      doc,
      kind === 'purchase' ? 'DELIVER TO' : 'SHIP TO',
      rightLines,
      !consignee,
      this.margin + partyWidth + 6,
      y,
      partyWidth,
      cardHeight,
    );
    y += cardHeight + 7;

    // ── Line items ───────────────────────────────────────────────────────
    const items: OrderItemRow[] = Array.isArray(order.productDetailesList) ? order.productDetailesList : [];
    const lineTotal = (it: OrderItemRow): number =>
      it.totalPrice != null && !Number.isNaN(it.totalPrice) ? it.totalPrice : (it.qty ?? 0) * (it.unitPrice ?? 0);
    const showFail = items.some(it => !!it.actuatorFailCondition);
    const money = currency ? ` (${currency})` : '';

    const head = ['#', 'Tag No', 'Description', 'Flange', 'Motor Duty', 'Control Voltage'];
    if (showFail) head.push('Fail Condition');
    head.push('Qty', `Unit Price${money}`, `Amount${money}`);

    const rows = items.map((it, i) => {
      const row = [
        String(it.serialNo ?? i + 1),
        it.tagNo ?? '',
        it.description ?? '',
        it.flange ?? '',
        it.actuatorMotorDuty ?? '',
        it.actuatorControlVoltage ?? '',
      ];
      if (showFail) row.push(it.actuatorFailCondition ?? '');
      row.push(this.num(it.qty ?? 0, 0, 3), this.num(it.unitPrice ?? 0), this.num(lineTotal(it)));
      return row;
    });
    const qtyCol = head.length - 3;

    autoTable(doc, {
      startY: y,
      head: [head],
      body: rows.length
        ? rows
        : [[{ content: 'No line items', colSpan: head.length, styles: { halign: 'center', textColor: this.muted } }]],
      theme: 'grid',
      styles: {
        fontSize: 7.8,
        cellPadding: { top: 1.8, bottom: 1.8, left: 1.5, right: 1.5 },
        overflow: 'linebreak',
        lineColor: this.line,
        lineWidth: 0.2,
        textColor: this.ink,
        valign: 'middle',
      },
      headStyles: { fillColor: this.brandDark, textColor: 255, fontStyle: 'bold', halign: 'left', lineColor: this.brandDark },
      alternateRowStyles: { fillColor: [250, 251, 253] },
      // Fixed widths for the short, code-like columns so "S2-15 min" or "415V 3Ph" stays on
      // one line; Description takes whatever is left and is the only column meant to wrap.
      columnStyles: {
        0: { cellWidth: 7, halign: 'center' },
        1: { cellWidth: 17 },
        2: { cellWidth: 'auto' },
        3: { cellWidth: 12 },
        4: { cellWidth: 18 },
        5: { cellWidth: 18 },
        ...(showFail ? { 6: { cellWidth: 18 } } : {}),
        [qtyCol]: { halign: 'right', cellWidth: 11 },
        [qtyCol + 1]: { halign: 'right', cellWidth: 21 },
        [qtyCol + 2]: { halign: 'right', cellWidth: 24, fontStyle: 'bold' },
      },
      didParseCell(data) {
        // Numeric headers read right-aligned over their figures.
        if (data.section === 'head' && data.column.index >= qtyCol) data.cell.styles.halign = 'right';
      },
      margin: { left: this.margin, right: this.margin, top: 20, bottom: 22 },
      // Repeat the header on every page rather than duplicating the whole first page, which
      // is what the old screenshot-based version did.
      showHead: 'everyPage',
      // A line item is one commitment; splitting it across a page break reads as two.
      rowPageBreak: 'avoid',
    });

    y = (doc as unknown as { lastAutoTable?: { finalY: number } }).lastAutoTable?.finalY ?? y + 20;

    // ── Totals ───────────────────────────────────────────────────────────
    const grandTotal = items.reduce((sum, it) => sum + lineTotal(it), 0);
    const totalQty = items.reduce((sum, it) => sum + (it.qty ?? 0), 0);
    const totalsWidth = 78;
    const totalsX = pageWidth - this.margin - totalsWidth;
    y = this.ensureRoom(doc, y + 5, 34);

    this.font(doc, 'normal', 8.5, this.muted);
    doc.text(`Line items: ${items.length}`, totalsX + 3, y + 4.5);
    doc.text(`Total quantity: ${this.num(totalQty, 0, 3)}`, pageWidth - this.margin - 3, y + 4.5, { align: 'right' });
    doc.setFillColor(...this.brandDark).rect(totalsX, y + 7, totalsWidth, 10, 'F');
    this.font(doc, 'bold', 10, [255, 255, 255]).text('GRAND TOTAL', totalsX + 3, y + 13.4);
    doc.text(`${currency ? currency + ' ' : ''}${this.num(grandTotal)}`, pageWidth - this.margin - 3, y + 13.4, { align: 'right' });

    const words = `${this.inWords(grandTotal)}${currency ? ' ' + currency : ''} only`;
    const wordsLines = doc.splitTextToSize(`Amount in words: ${words}`, contentWidth - totalsWidth - 8) as string[];
    this.font(doc, 'italic', 8.5, this.ink).text(wordsLines, this.margin, y + 9);
    y += Math.max(20, 9 + wordsLines.length * 4) + 6;

    // ── Terms ────────────────────────────────────────────────────────────
    const terms: [string, string | null | undefined][] = [
      ['Payment Terms', termsReq.paymentTerms],
      ['Delivery Terms', termsReq.deliveryTerms],
      ['Remarks', termsReq.remarks],
    ].filter(([, value]) => !!value) as [string, string][];
    if (terms.length) {
      y = this.ensureRoom(doc, y, 20);
      y = this.sectionTitle(doc, 'TERMS & CONDITIONS', y);
      for (const [label, value] of terms) {
        const wrapped = doc.splitTextToSize(value ?? '', contentWidth - 38) as string[];
        y = this.ensureRoom(doc, y, wrapped.length * 4.4 + 2);
        this.font(doc, 'bold', 9, this.ink).text(label, this.margin, y);
        this.font(doc, 'normal', 9, this.ink).text(wrapped, this.margin + 38, y);
        y += Math.max(5.5, wrapped.length * 4.4) + 1.5;
      }
    }

    // ── Signatures ───────────────────────────────────────────────────────
    // Sized to what it actually occupies (heading, 14 mm to sign in, caption), so it does not
    // push itself onto a page of its own when the terms end near the bottom.
    y = this.ensureRoom(doc, y + 6, 24);
    const sigWidth = 64;
    const sigY = y + 14;
    doc.setDrawColor(...this.muted).setLineWidth(0.3);
    doc.line(this.margin, sigY, this.margin + sigWidth, sigY);
    doc.line(pageWidth - this.margin - sigWidth, sigY, pageWidth - this.margin, sigY);
    this.font(doc, 'bold', 9, this.ink);
    doc.text(kind === 'purchase' ? 'Supplier Acceptance' : 'Customer Acceptance', this.margin, y);
    doc.text('For SCHIEBEL', pageWidth - this.margin, y, { align: 'right' });
    this.font(doc, 'normal', 8, this.muted);
    doc.text('Signature, name & date', this.margin, sigY + 4.5);
    doc.text('Authorised Signatory', pageWidth - this.margin, sigY + 4.5, { align: 'right' });

    // ── Footer, written after the fact so the page count is known ────────
    const pages = doc.getNumberOfPages();
    for (let p = 1; p <= pages; p++) {
      doc.setPage(p);
      const fy = pageHeight - 10;
      doc
        .setDrawColor(...this.line)
        .setLineWidth(0.3)
        .line(this.margin, fy - 4, pageWidth - this.margin, fy - 4);
      this.font(doc, 'normal', 7.5, this.muted);
      doc.text(`${orderNo}${order.version != null ? `  ·  Version ${order.version}` : ''}`, this.margin, fy);
      doc.text('This is a computer-generated document.', pageWidth / 2, fy, { align: 'center' });
      doc.text(`Page ${p} of ${pages}`, pageWidth - this.margin, fy, { align: 'right' });
    }

    const name = `${kind === 'purchase' ? 'PurchaseOrder' : 'SalesOrder'}-${order.refNo ?? String(order.id)}`;
    doc.save(`${name.replace(/[^\w.-]+/g, '_')}.pdf`);
  }

  // ── Layout helpers ─────────────────────────────────────────────────────

  private font(doc: jsPDF, style: 'normal' | 'bold' | 'italic', size: number, color: Rgb): jsPDF {
    return doc
      .setFont('helvetica', style)
      .setFontSize(size)
      .setTextColor(...color);
  }

  private box(doc: jsPDF, x: number, y: number, w: number, h: number, fill: Rgb): void {
    doc
      .setFillColor(...fill)
      .setDrawColor(...this.line)
      .setLineWidth(0.2);
    doc.roundedRect(x, y, w, h, 1.5, 1.5, 'FD');
  }

  private sectionTitle(doc: jsPDF, text: string, y: number): number {
    this.font(doc, 'bold', 8.5, this.brandDark).text(text, this.margin, y);
    doc
      .setDrawColor(...this.line)
      .setLineWidth(0.3)
      .line(this.margin, y + 1.8, doc.internal.pageSize.getWidth() - this.margin, y + 1.8);
    return y + 7.5;
  }

  /** A party's lines as the card prints them: name, wrapped address, email, phone. */
  private partyLines(
    doc: jsPDF,
    party: OrderPartyBlock | null | undefined,
    placeholder: string,
    width: number,
  ): { text: string; bold?: boolean }[] {
    const lines: { text: string; bold?: boolean }[] = [];
    if (party?.name) lines.push({ text: party.name, bold: true });
    if (party?.address) {
      (doc.setFontSize(9).splitTextToSize(party.address, width - 8) as string[]).forEach(t => lines.push({ text: t }));
    }
    if (party?.email) lines.push({ text: party.email });
    if (party?.phoneNumber) lines.push({ text: `Tel: ${party.phoneNumber}` });
    if (!lines.length) lines.push({ text: placeholder });
    return lines;
  }

  private partyCard(
    doc: jsPDF,
    label: string,
    lines: { text: string; bold?: boolean }[],
    isPlaceholder: boolean,
    x: number,
    y: number,
    width: number,
    height: number,
  ): void {
    this.box(doc, x, y, width, height, [255, 255, 255]);
    doc.setFillColor(...this.brand).rect(x, y, 1.4, height, 'F');
    this.font(doc, 'bold', 7.5, this.brandDark).text(label, x + 5, y + 5.5);
    lines.forEach((l, i) => {
      this.font(doc, l.bold ? 'bold' : 'normal', 9, isPlaceholder ? this.muted : this.ink).text(l.text, x + 5, y + 10.5 + i * 4.6);
    });
  }

  /** Starts a new page when fewer than `needed` mm remain above the footer rule. */
  private ensureRoom(doc: jsPDF, y: number, needed: number): number {
    if (y + needed > doc.internal.pageSize.getHeight() - 17) {
      doc.addPage();
      return 22;
    }
    return y;
  }

  private hasParty(party: OrderPartyBlock | null | undefined): boolean {
    return !!party && [party.name, party.address, party.email, party.phoneNumber].some(v => !!v);
  }

  private loadLogo(): Promise<string | null> {
    this.logo ??= fetch('content/images/brand/schiebel-logo.png')
      .then(res => (res.ok ? res.blob() : Promise.reject(new Error(String(res.status)))))
      .then(
        blob =>
          new Promise<string | null>(resolve => {
            const reader = new FileReader();
            reader.onload = () => resolve(typeof reader.result === 'string' ? reader.result : null);
            reader.onerror = () => resolve(null);
            reader.readAsDataURL(blob);
          }),
      )
      .catch(() => null);
    return this.logo;
  }

  // ── Formatting ─────────────────────────────────────────────────────────

  /** "22 Sep 2026" - built by hand, because en-GB renders September as "Sept". */
  private date(value: string | null | undefined): string {
    if (!value) {
      return '-';
    }
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) {
      return value.slice(0, 10);
    }
    return `${String(d.getDate()).padStart(2, '0')} ${OrderPdfService.MONTHS[d.getMonth()]} ${d.getFullYear()}`;
  }

  private num(value: number, min = 2, max = 2): string {
    return value.toLocaleString('en-US', { minimumFractionDigits: min, maximumFractionDigits: max });
  }

  private clip(value: string, max: number): string {
    return value.length > max ? value.slice(0, max - 1) + '…' : value;
  }

  /**
   * An amount in words, international scale to match the figures' 1,000,000 grouping:
   * 1000000.5 -> "One Million and 50/100".
   */
  private inWords(amount: number): string {
    const whole = Math.floor(Math.abs(amount));
    const cents = Math.round((Math.abs(amount) - whole) * 100);
    const ones = [
      '',
      'One',
      'Two',
      'Three',
      'Four',
      'Five',
      'Six',
      'Seven',
      'Eight',
      'Nine',
      'Ten',
      'Eleven',
      'Twelve',
      'Thirteen',
      'Fourteen',
      'Fifteen',
      'Sixteen',
      'Seventeen',
      'Eighteen',
      'Nineteen',
    ];
    const tens = ['', '', 'Twenty', 'Thirty', 'Forty', 'Fifty', 'Sixty', 'Seventy', 'Eighty', 'Ninety'];
    const below1000 = (n: number): string => {
      const parts: string[] = [];
      if (n >= 100) {
        parts.push(`${ones[Math.floor(n / 100)]} Hundred`);
        n %= 100;
      }
      if (n >= 20) {
        parts.push(tens[Math.floor(n / 10)] + (n % 10 ? `-${ones[n % 10]}` : ''));
      } else if (n > 0) {
        parts.push(ones[n]);
      }
      return parts.join(' ');
    };
    const scales = ['', 'Thousand', 'Million', 'Billion', 'Trillion'];
    let words: string;
    if (whole === 0) {
      words = 'Zero';
    } else {
      const groups: string[] = [];
      let n = whole;
      let scale = 0;
      while (n > 0 && scale < scales.length) {
        const chunk = n % 1000;
        if (chunk) groups.unshift(`${below1000(chunk)}${scales[scale] ? ' ' + scales[scale] : ''}`);
        n = Math.floor(n / 1000);
        scale++;
      }
      words = groups.join(' ');
    }
    return cents ? `${words} and ${String(cents).padStart(2, '0')}/100` : words;
  }
}
