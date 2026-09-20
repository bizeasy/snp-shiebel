import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteItem } from '../quote-item.model';
import { QuoteItemService } from '../service/quote-item.service';

import { QuoteItemFormGroup, QuoteItemFormService } from './quote-item-form.service';

@Component({
  selector: 'sys-quote-item-update',
  templateUrl: './quote-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  quoteItem: IQuoteItem | null = null;

  quotesSharedCollection = signal<IQuote[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  custRequestsSharedCollection = signal<ICustRequest[]>([]);
  custRequestItemsSharedCollection = signal<ICustRequestItem[]>([]);

  protected quoteItemService = inject(QuoteItemService);
  protected quoteItemFormService = inject(QuoteItemFormService);
  protected quoteService = inject(QuoteService);
  protected productService = inject(ProductService);
  protected productFeatureService = inject(ProductFeatureService);
  protected uomService = inject(UomService);
  protected statusService = inject(StatusService);
  protected custRequestService = inject(CustRequestService);
  protected custRequestItemService = inject(CustRequestItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteItemFormGroup = this.quoteItemFormService.createQuoteItemFormGroup();

  compareQuote = (o1: IQuote | null, o2: IQuote | null): boolean => this.quoteService.compareQuote(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareCustRequest = (o1: ICustRequest | null, o2: ICustRequest | null): boolean => this.custRequestService.compareCustRequest(o1, o2);

  compareCustRequestItem = (o1: ICustRequestItem | null, o2: ICustRequestItem | null): boolean =>
    this.custRequestItemService.compareCustRequestItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteItem }) => {
      this.quoteItem = quoteItem;
      if (quoteItem) {
        this.updateForm(quoteItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quoteItem = this.quoteItemFormService.getQuoteItem(this.editForm);
    if (quoteItem.id === null) {
      this.subscribeToSaveResponse(this.quoteItemService.create(quoteItem));
    } else {
      this.subscribeToSaveResponse(this.quoteItemService.update(quoteItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuoteItem | null>): void {
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

  protected updateForm(quoteItem: IQuoteItem): void {
    this.quoteItem = quoteItem;
    this.quoteItemFormService.resetForm(this.editForm, quoteItem);

    this.quotesSharedCollection.update(quotes => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, quoteItem.quote));
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, quoteItem.product),
    );
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(productFeatures, quoteItem.productFeature),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, quoteItem.uom));
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, quoteItem.status),
    );
    this.custRequestsSharedCollection.update(custRequests =>
      this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, quoteItem.custRequest),
    );
    this.custRequestItemsSharedCollection.update(custRequestItems =>
      this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(custRequestItems, quoteItem.custRequestItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quoteService
      .query()
      .pipe(map((res: HttpResponse<IQuote[]>) => res.body ?? []))
      .pipe(map((quotes: IQuote[]) => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, this.quoteItem?.quote)))
      .subscribe((quotes: IQuote[]) => this.quotesSharedCollection.set(quotes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.quoteItem?.product)))
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.quoteItem?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.quoteItem?.uom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.quoteItem?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.custRequestService
      .query()
      .pipe(map((res: HttpResponse<ICustRequest[]>) => res.body ?? []))
      .pipe(
        map((custRequests: ICustRequest[]) =>
          this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, this.quoteItem?.custRequest),
        ),
      )
      .subscribe((custRequests: ICustRequest[]) => this.custRequestsSharedCollection.set(custRequests));

    this.custRequestItemService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestItem[]>) => res.body ?? []))
      .pipe(
        map((custRequestItems: ICustRequestItem[]) =>
          this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(
            custRequestItems,
            this.quoteItem?.custRequestItem,
          ),
        ),
      )
      .subscribe((custRequestItems: ICustRequestItem[]) => this.custRequestItemsSharedCollection.set(custRequestItems));
  }
}
