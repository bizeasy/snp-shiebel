import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';
import { InvoiceTypeService } from 'app/entities/invoice-type/service/invoice-type.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';

import { InvoiceFormGroup, InvoiceFormService } from './invoice-form.service';

@Component({
  selector: 'sys-invoice-update',
  templateUrl: './invoice-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoice: IInvoice | null = null;

  invoiceTypesSharedCollection = signal<IInvoiceType[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected invoiceService = inject(InvoiceService);
  protected invoiceFormService = inject(InvoiceFormService);
  protected invoiceTypeService = inject(InvoiceTypeService);
  protected orderService = inject(OrderService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected statusService = inject(StatusService);
  protected contactMechService = inject(ContactMechService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  compareInvoiceType = (o1: IInvoiceType | null, o2: IInvoiceType | null): boolean => this.invoiceTypeService.compareInvoiceType(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('snpApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id === null) {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoice | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.invoiceTypesSharedCollection.update(invoiceTypes =>
      this.invoiceTypeService.addInvoiceTypeToCollectionIfMissing<IInvoiceType>(invoiceTypes, invoice.invoiceType),
    );
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, invoice.order));
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, invoice.fromParty, invoice.toParty),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, invoice.roleType),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, invoice.status));
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, invoice.contactMech),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, invoice.currencyUom));
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceTypeService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceType[]>) => res.body ?? []))
      .pipe(
        map((invoiceTypes: IInvoiceType[]) =>
          this.invoiceTypeService.addInvoiceTypeToCollectionIfMissing<IInvoiceType>(invoiceTypes, this.invoice?.invoiceType),
        ),
      )
      .subscribe((invoiceTypes: IInvoiceType[]) => this.invoiceTypesSharedCollection.set(invoiceTypes));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.invoice?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.invoice?.fromParty, this.invoice?.toParty),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.invoice?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.invoice?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.invoice?.contactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.invoice?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
