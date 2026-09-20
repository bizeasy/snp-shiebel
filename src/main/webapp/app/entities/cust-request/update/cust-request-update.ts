import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustRequestCategory } from 'app/entities/cust-request-category/cust-request-category.model';
import { CustRequestCategoryService } from 'app/entities/cust-request-category/service/cust-request-category.service';
import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';
import { CustRequestTypeService } from 'app/entities/cust-request-type/service/cust-request-type.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequest } from '../cust-request.model';
import { CustRequestService } from '../service/cust-request.service';

import { CustRequestFormGroup, CustRequestFormService } from './cust-request-form.service';

@Component({
  selector: 'sys-cust-request-update',
  templateUrl: './cust-request-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequest: ICustRequest | null = null;

  custRequestTypesSharedCollection = signal<ICustRequestType[]>([]);
  custRequestCategoriesSharedCollection = signal<ICustRequestCategory[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  productStoresSharedCollection = signal<IProductStore[]>([]);
  salesChannelsSharedCollection = signal<ISalesChannel[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected custRequestService = inject(CustRequestService);
  protected custRequestFormService = inject(CustRequestFormService);
  protected custRequestTypeService = inject(CustRequestTypeService);
  protected custRequestCategoryService = inject(CustRequestCategoryService);
  protected statusService = inject(StatusService);
  protected partyService = inject(PartyService);
  protected productStoreService = inject(ProductStoreService);
  protected salesChannelService = inject(SalesChannelService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestFormGroup = this.custRequestFormService.createCustRequestFormGroup();

  compareCustRequestType = (o1: ICustRequestType | null, o2: ICustRequestType | null): boolean =>
    this.custRequestTypeService.compareCustRequestType(o1, o2);

  compareCustRequestCategory = (o1: ICustRequestCategory | null, o2: ICustRequestCategory | null): boolean =>
    this.custRequestCategoryService.compareCustRequestCategory(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareProductStore = (o1: IProductStore | null, o2: IProductStore | null): boolean =>
    this.productStoreService.compareProductStore(o1, o2);

  compareSalesChannel = (o1: ISalesChannel | null, o2: ISalesChannel | null): boolean =>
    this.salesChannelService.compareSalesChannel(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequest }) => {
      this.custRequest = custRequest;
      if (custRequest) {
        this.updateForm(custRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequest = this.custRequestFormService.getCustRequest(this.editForm);
    if (custRequest.id === null) {
      this.subscribeToSaveResponse(this.custRequestService.create(custRequest));
    } else {
      this.subscribeToSaveResponse(this.custRequestService.update(custRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequest | null>): void {
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

  protected updateForm(custRequest: ICustRequest): void {
    this.custRequest = custRequest;
    this.custRequestFormService.resetForm(this.editForm, custRequest);

    this.custRequestTypesSharedCollection.update(custRequestTypes =>
      this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(custRequestTypes, custRequest.custRequestType),
    );
    this.custRequestCategoriesSharedCollection.update(custRequestCategories =>
      this.custRequestCategoryService.addCustRequestCategoryToCollectionIfMissing<ICustRequestCategory>(
        custRequestCategories,
        custRequest.custRequestCategory,
      ),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, custRequest.status),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, custRequest.fromParty));
    this.productStoresSharedCollection.update(productStores =>
      this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, custRequest.productStore),
    );
    this.salesChannelsSharedCollection.update(salesChannels =>
      this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, custRequest.salesChannel),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, custRequest.currencyUom));
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestTypeService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestType[]>) => res.body ?? []))
      .pipe(
        map((custRequestTypes: ICustRequestType[]) =>
          this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(
            custRequestTypes,
            this.custRequest?.custRequestType,
          ),
        ),
      )
      .subscribe((custRequestTypes: ICustRequestType[]) => this.custRequestTypesSharedCollection.set(custRequestTypes));

    this.custRequestCategoryService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestCategory[]>) => res.body ?? []))
      .pipe(
        map((custRequestCategories: ICustRequestCategory[]) =>
          this.custRequestCategoryService.addCustRequestCategoryToCollectionIfMissing<ICustRequestCategory>(
            custRequestCategories,
            this.custRequest?.custRequestCategory,
          ),
        ),
      )
      .subscribe((custRequestCategories: ICustRequestCategory[]) => this.custRequestCategoriesSharedCollection.set(custRequestCategories));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.custRequest?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.custRequest?.fromParty)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.productStoreService
      .query()
      .pipe(map((res: HttpResponse<IProductStore[]>) => res.body ?? []))
      .pipe(
        map((productStores: IProductStore[]) =>
          this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, this.custRequest?.productStore),
        ),
      )
      .subscribe((productStores: IProductStore[]) => this.productStoresSharedCollection.set(productStores));

    this.salesChannelService
      .query()
      .pipe(map((res: HttpResponse<ISalesChannel[]>) => res.body ?? []))
      .pipe(
        map((salesChannels: ISalesChannel[]) =>
          this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, this.custRequest?.salesChannel),
        ),
      )
      .subscribe((salesChannels: ISalesChannel[]) => this.salesChannelsSharedCollection.set(salesChannels));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.custRequest?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
