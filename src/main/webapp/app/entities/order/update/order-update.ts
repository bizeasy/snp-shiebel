import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { ICustomTimePeriod } from 'app/entities/custom-time-period/custom-time-period.model';
import { CustomTimePeriodService } from 'app/entities/custom-time-period/service/custom-time-period.service';
import { IDepartment } from 'app/entities/department/department.model';
import { IFacility } from 'app/entities/facility/facility.model';
import { IOrderType } from 'app/entities/order-type/order-type.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';

import { OrderFormGroup, OrderFormService } from './order-form.service';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { OrderTypeService } from 'app/entities/order-type/service/order-type.service';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';
import { UomConversionService } from 'app/entities/uom-conversion/service/uom-conversion.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'sys-order-update',
  templateUrl: './order-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class OrderUpdate implements OnInit {
  readonly isSaving = signal(false);
  order: IOrder | null = null;

  orderTypesSharedCollection = signal<IOrderType[]>([]);
  salesChannelsSharedCollection = signal<ISalesChannel[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  uomConversionsSharedCollection = signal<IUomConversion[]>([]);
  quotesSharedCollection = signal<IQuote[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  productStoresSharedCollection = signal<IProductStore[]>([]);
  departmentsSharedCollection = signal<IDepartment[]>([]);
  customTimePeriodsSharedCollection = signal<ICustomTimePeriod[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected orderService = inject(OrderService);
  protected orderFormService = inject(OrderFormService);
  protected orderTypeService = inject(OrderTypeService);
  protected salesChannelService = inject(SalesChannelService);
  protected partyService = inject(PartyService);
  protected statusService = inject(StatusService);
  protected uomService = inject(UomService);
  protected uomConversionService = inject(UomConversionService);
  protected quoteService = inject(QuoteService);
  protected facilityService = inject(FacilityService);
  protected productStoreService = inject(ProductStoreService);
  protected departmentService = inject(DepartmentService);
  protected customTimePeriodService = inject(CustomTimePeriodService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  compareOrderType = (o1: IOrderType | null, o2: IOrderType | null): boolean => this.orderTypeService.compareOrderType(o1, o2);

  compareSalesChannel = (o1: ISalesChannel | null, o2: ISalesChannel | null): boolean =>
    this.salesChannelService.compareSalesChannel(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareUomConversion = (o1: IUomConversion | null, o2: IUomConversion | null): boolean =>
    this.uomConversionService.compareUomConversion(o1, o2);

  compareQuote = (o1: IQuote | null, o2: IQuote | null): boolean => this.quoteService.compareQuote(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareProductStore = (o1: IProductStore | null, o2: IProductStore | null): boolean =>
    this.productStoreService.compareProductStore(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareCustomTimePeriod = (o1: ICustomTimePeriod | null, o2: ICustomTimePeriod | null): boolean =>
    this.customTimePeriodService.compareCustomTimePeriod(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
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
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id === null) {
      this.subscribeToSaveResponse(this.orderService.create(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.update(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrder | null>): void {
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

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.orderTypesSharedCollection.update(orderTypes =>
      this.orderTypeService.addOrderTypeToCollectionIfMissing<IOrderType>(orderTypes, order.orderType),
    );
    this.salesChannelsSharedCollection.update(salesChannels =>
      this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, order.salesChannel),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, order.party, order.supplier),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, order.status));
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, order.currencyUom));
    this.uomConversionsSharedCollection.update(uomConversions =>
      this.uomConversionService.addUomConversionToCollectionIfMissing<IUomConversion>(uomConversions, order.currencyConversion),
    );
    this.quotesSharedCollection.update(quotes => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, order.quote));
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, order.facility),
    );
    this.productStoresSharedCollection.update(productStores =>
      this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, order.productStore),
    );
    this.departmentsSharedCollection.update(departments =>
      this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, order.department),
    );
    this.customTimePeriodsSharedCollection.update(customTimePeriods =>
      this.customTimePeriodService.addCustomTimePeriodToCollectionIfMissing<ICustomTimePeriod>(customTimePeriods, order.customTimePeriod),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderTypeService
      .query()
      .pipe(map((res: HttpResponse<IOrderType[]>) => res.body ?? []))
      .pipe(
        map((orderTypes: IOrderType[]) =>
          this.orderTypeService.addOrderTypeToCollectionIfMissing<IOrderType>(orderTypes, this.order?.orderType),
        ),
      )
      .subscribe((orderTypes: IOrderType[]) => this.orderTypesSharedCollection.set(orderTypes));

    this.salesChannelService
      .query()
      .pipe(map((res: HttpResponse<ISalesChannel[]>) => res.body ?? []))
      .pipe(
        map((salesChannels: ISalesChannel[]) =>
          this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, this.order?.salesChannel),
        ),
      )
      .subscribe((salesChannels: ISalesChannel[]) => this.salesChannelsSharedCollection.set(salesChannels));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.order?.party, this.order?.supplier),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.order?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.order?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.uomConversionService
      .query()
      .pipe(map((res: HttpResponse<IUomConversion[]>) => res.body ?? []))
      .pipe(
        map((uomConversions: IUomConversion[]) =>
          this.uomConversionService.addUomConversionToCollectionIfMissing<IUomConversion>(uomConversions, this.order?.currencyConversion),
        ),
      )
      .subscribe((uomConversions: IUomConversion[]) => this.uomConversionsSharedCollection.set(uomConversions));

    this.quoteService
      .query()
      .pipe(map((res: HttpResponse<IQuote[]>) => res.body ?? []))
      .pipe(map((quotes: IQuote[]) => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, this.order?.quote)))
      .subscribe((quotes: IQuote[]) => this.quotesSharedCollection.set(quotes));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.order?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.productStoreService
      .query()
      .pipe(map((res: HttpResponse<IProductStore[]>) => res.body ?? []))
      .pipe(
        map((productStores: IProductStore[]) =>
          this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, this.order?.productStore),
        ),
      )
      .subscribe((productStores: IProductStore[]) => this.productStoresSharedCollection.set(productStores));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.order?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => this.departmentsSharedCollection.set(departments));

    this.customTimePeriodService
      .query()
      .pipe(map((res: HttpResponse<ICustomTimePeriod[]>) => res.body ?? []))
      .pipe(
        map((customTimePeriods: ICustomTimePeriod[]) =>
          this.customTimePeriodService.addCustomTimePeriodToCollectionIfMissing<ICustomTimePeriod>(
            customTimePeriods,
            this.order?.customTimePeriod,
          ),
        ),
      )
      .subscribe((customTimePeriods: ICustomTimePeriod[]) => this.customTimePeriodsSharedCollection.set(customTimePeriods));
  }
}
