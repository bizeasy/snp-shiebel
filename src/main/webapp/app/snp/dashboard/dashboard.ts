import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { DashboardService, DashboardSummary } from './dashboard.service';

/** One bar in the cash-book chart, pre-scaled so the template does no arithmetic. */
interface SpendBar {
  category: string;
  amount: number;
  percent: number;
}

/** One point on the order-value chart, in SVG user units. */
interface SeriesPoint {
  month: string;
  label: string;
  sales: number;
  purchase: number;
  x: number;
  ySales: number;
  yPurchase: number;
  /** False when the month tick is suppressed to stop axis labels colliding. */
  showLabel: boolean;
}

/**
 * Dashboard.
 *
 * Rebuilt rather than ported. The snp21 dashboard fetched
 * /api/facilities/facilities-list/search, which no controller served, and its two charts
 * plotted hard-coded demo data (categories A..E, months January..July) for a facilities
 * domain this application does not have. Everything below is computed from real rows via
 * /api/dashboard/summary.
 *
 * Forms follow the data:
 *  - headline counts and values are stat tiles, not one-bar charts;
 *  - sales vs purchase value over time is a two-series line (both are currency, so they
 *    share one axis - never a second y-scale);
 *  - cash-book spend by category is a horizontal bar, where length already encodes
 *    magnitude, so every bar takes the same hue rather than a ramp.
 *
 * Colours are the validated pair #2a78d6 / #eb6834 (CVD deltaE 24.7 protan, 33.6 normal,
 * both above 3:1 on white). Each series is also direct-labelled, so identity never rests
 * on colour alone.
 */
@Component({
  selector: 'sys-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [RouterLink, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Dashboard {
  readonly data = signal<DashboardSummary | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly months = signal(12);
  readonly hovered = signal<SeriesPoint | null>(null);

  readonly monthOptions = [3, 6, 12, 24];

  /** Validated categorical pair; index 0 = sales, 1 = purchase. */
  readonly colourSales = '#2a78d6';
  readonly colourPurchase = '#eb6834';

  private readonly service = inject(DashboardService);

  // chart geometry in SVG user units
  private readonly W = 720;
  private readonly H = 240;
  private readonly PAD_L = 56;
  private readonly PAD_R = 16;
  private readonly PAD_T = 16;
  private readonly PAD_B = 28;

  readonly kpis = computed(() => this.data()?.kpis ?? null);
  readonly recent = computed(() => this.data()?.recentSalesOrders ?? []);

  readonly hasOrderData = computed(() => (this.data()?.orderValueByMonth ?? []).some(p => p.sales > 0 || p.purchase > 0));

  readonly yMax = computed(() => {
    const pts = this.data()?.orderValueByMonth ?? [];
    const max = Math.max(0, ...pts.map(p => Math.max(p.sales, p.purchase)));
    return max === 0 ? 1 : max;
  });

  readonly points = computed<SeriesPoint[]>(() => {
    const pts = this.data()?.orderValueByMonth ?? [];
    if (pts.length === 0) {
      return [];
    }
    const max = this.yMax();
    const innerW = this.W - this.PAD_L - this.PAD_R;
    const innerH = this.H - this.PAD_T - this.PAD_B;
    const step = pts.length === 1 ? 0 : innerW / (pts.length - 1);
    // At 24 months the ticks would overlap, so keep about eight of them.
    const labelStep = Math.ceil(pts.length / 8);
    const last = pts.length - 1;
    return pts.map((p, i) => ({
      month: p.month,
      label: this.monthLabel(p.month),
      sales: p.sales,
      purchase: p.purchase,
      x: this.PAD_L + step * i,
      ySales: this.PAD_T + innerH - (p.sales / max) * innerH,
      yPurchase: this.PAD_T + innerH - (p.purchase / max) * innerH,
      showLabel: (last - i) % labelStep === 0,
    }));
  });

  readonly salesPath = computed(() => this.toPath(p => p.ySales));
  readonly purchasePath = computed(() => this.toPath(p => p.yPurchase));

  /** Four gridlines plus the baseline, labelled with the value they represent. */
  readonly gridLines = computed(() => {
    const innerH = this.H - this.PAD_T - this.PAD_B;
    const max = this.yMax();
    return [0, 0.25, 0.5, 0.75, 1].map(f => ({
      y: this.PAD_T + innerH - f * innerH,
      value: max * f,
    }));
  });

  readonly spendBars = computed<SpendBar[]>(() => {
    const rows = this.data()?.cashBookByCategory ?? [];
    const max = Math.max(0, ...rows.map(r => r.amount));
    return rows.map(r => ({
      category: r.category,
      amount: r.amount,
      percent: max === 0 ? 0 : (r.amount / max) * 100,
    }));
  });

  readonly spendTotal = computed(() => (this.data()?.cashBookByCategory ?? []).reduce((s, r) => s + r.amount, 0));

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.service.summary(this.months()).subscribe({
      next: d => {
        this.data.set(d);
        this.loading.set(false);
      },
      error: () => {
        this.data.set(null);
        this.error.set('Could not load the dashboard.');
        this.loading.set(false);
      },
    });
  }

  changeMonths(value: number): void {
    this.months.set(value);
    this.load();
  }

  onHover(p: SeriesPoint | null): void {
    this.hovered.set(p);
  }

  /** Chart geometry, exposed for the template. */
  get width(): number {
    return this.W;
  }
  get height(): number {
    return this.H;
  }
  get padLeft(): number {
    return this.PAD_L;
  }
  get plotRight(): number {
    return this.W - this.PAD_R;
  }
  get plotBottom(): number {
    return this.H - this.PAD_B;
  }

  private toPath(pick: (p: SeriesPoint) => number): string {
    const pts = this.points();
    if (pts.length === 0) {
      return '';
    }
    return pts.map((p, i) => `${i === 0 ? 'M' : 'L'}${p.x.toFixed(1)},${pick(p).toFixed(1)}`).join(' ');
  }

  private monthLabel(ym: string): string {
    const [year, month] = ym.split('-').map(Number);
    const names = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${names[(month || 1) - 1]} ${String(year).slice(2)}`;
  }
}
