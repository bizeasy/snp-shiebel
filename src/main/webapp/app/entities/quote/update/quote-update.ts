import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { IQuoteType } from 'app/entities/quote-type/quote-type.model';
import { QuoteTypeService } from 'app/entities/quote-type/service/quote-type.service';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomConversionService } from 'app/entities/uom-conversion/service/uom-conversion.service';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';

import { QuoteFormGroup, QuoteFormService } from './quote-form.service';

@Component({
  selector: 'sys-quote-update',
  templateUrl: './quote-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteUpdate implements OnInit {
  readonly isSaving = signal(false);
  quote: IQuote | null = null;

  quoteTypesSharedCollection = signal<IQuoteType[]>([]);
  custRequestsSharedCollection = signal<ICustRequest[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  productStoresSharedCollection = signal<IProductStore[]>([]);
  salesChannelsSharedCollection = signal<ISalesChannel[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  uomConversionsSharedCollection = signal<IUomConversion[]>([]);

  protected quoteService = inject(QuoteService);
  protected quoteFormService = inject(QuoteFormService);
  protected quoteTypeService = inject(QuoteTypeService);
  protected custRequestService = inject(CustRequestService);
  protected partyService = inject(PartyService);
  protected statusService = inject(StatusService);
  protected productStoreService = inject(ProductStoreService);
  protected salesChannelService = inject(SalesChannelService);
  protected uomService = inject(UomService);
  protected uomConversionService = inject(UomConversionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteFormGroup = this.quoteFormService.createQuoteFormGroup();

  compareQuoteType = (o1: IQuoteType | null, o2: IQuoteType | null): boolean => this.quoteTypeService.compareQuoteType(o1, o2);

  compareCustRequest = (o1: ICustRequest | null, o2: ICustRequest | null): boolean => this.custRequestService.compareCustRequest(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareProductStore = (o1: IProductStore | null, o2: IProductStore | null): boolean =>
    this.productStoreService.compareProductStore(o1, o2);

  compareSalesChannel = (o1: ISalesChannel | null, o2: ISalesChannel | null): boolean =>
    this.salesChannelService.compareSalesChannel(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareUomConversion = (o1: IUomConversion | null, o2: IUomConversion | null): boolean =>
    this.uomConversionService.compareUomConversion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quote }) => {
      this.quote = quote;
      if (quote) {
        this.updateForm(quote);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quote = this.quoteFormService.getQuote(this.editForm);
    if (quote.id === null) {
      this.subscribeToSaveResponse(this.quoteService.create(quote));
    } else {
      this.subscribeToSaveResponse(this.quoteService.update(quote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuote | null>): void {
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

  protected updateForm(quote: IQuote): void {
    this.quote = quote;
    this.quoteFormService.resetForm(this.editForm, quote);

    this.quoteTypesSharedCollection.update(quoteTypes =>
      this.quoteTypeService.addQuoteTypeToCollectionIfMissing<IQuoteType>(quoteTypes, quote.quoteType),
    );
    this.custRequestsSharedCollection.update(custRequests =>
      this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, quote.custRequest),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, quote.party));
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, quote.status));
    this.productStoresSharedCollection.update(productStores =>
      this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, quote.productStore),
    );
    this.salesChannelsSharedCollection.update(salesChannels =>
      this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, quote.salesChannel),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, quote.currencyUom));
    this.uomConversionsSharedCollection.update(uomConversions =>
      this.uomConversionService.addUomConversionToCollectionIfMissing<IUomConversion>(uomConversions, quote.currencyConversion),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quoteTypeService
      .query()
      .pipe(map((res: HttpResponse<IQuoteType[]>) => res.body ?? []))
      .pipe(
        map((quoteTypes: IQuoteType[]) =>
          this.quoteTypeService.addQuoteTypeToCollectionIfMissing<IQuoteType>(quoteTypes, this.quote?.quoteType),
        ),
      )
      .subscribe((quoteTypes: IQuoteType[]) => this.quoteTypesSharedCollection.set(quoteTypes));

    this.custRequestService
      .query()
      .pipe(map((res: HttpResponse<ICustRequest[]>) => res.body ?? []))
      .pipe(
        map((custRequests: ICustRequest[]) =>
          this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, this.quote?.custRequest),
        ),
      )
      .subscribe((custRequests: ICustRequest[]) => this.custRequestsSharedCollection.set(custRequests));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.quote?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.quote?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.productStoreService
      .query()
      .pipe(map((res: HttpResponse<IProductStore[]>) => res.body ?? []))
      .pipe(
        map((productStores: IProductStore[]) =>
          this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, this.quote?.productStore),
        ),
      )
      .subscribe((productStores: IProductStore[]) => this.productStoresSharedCollection.set(productStores));

    this.salesChannelService
      .query()
      .pipe(map((res: HttpResponse<ISalesChannel[]>) => res.body ?? []))
      .pipe(
        map((salesChannels: ISalesChannel[]) =>
          this.salesChannelService.addSalesChannelToCollectionIfMissing<ISalesChannel>(salesChannels, this.quote?.salesChannel),
        ),
      )
      .subscribe((salesChannels: ISalesChannel[]) => this.salesChannelsSharedCollection.set(salesChannels));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.quote?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.uomConversionService
      .query()
      .pipe(map((res: HttpResponse<IUomConversion[]>) => res.body ?? []))
      .pipe(
        map((uomConversions: IUomConversion[]) =>
          this.uomConversionService.addUomConversionToCollectionIfMissing<IUomConversion>(uomConversions, this.quote?.currencyConversion),
        ),
      )
      .subscribe((uomConversions: IUomConversion[]) => this.uomConversionsSharedCollection.set(uomConversions));
  }
}
