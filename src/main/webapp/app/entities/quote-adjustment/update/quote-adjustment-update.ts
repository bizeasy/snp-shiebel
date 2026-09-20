import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { OrderAdjustmentTypeService } from 'app/entities/order-adjustment-type/service/order-adjustment-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteAdjustment } from '../quote-adjustment.model';
import { QuoteAdjustmentService } from '../service/quote-adjustment.service';

import { QuoteAdjustmentFormGroup, QuoteAdjustmentFormService } from './quote-adjustment-form.service';

@Component({
  selector: 'sys-quote-adjustment-update',
  templateUrl: './quote-adjustment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteAdjustmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  quoteAdjustment: IQuoteAdjustment | null = null;

  quotesSharedCollection = signal<IQuote[]>([]);
  quoteItemsSharedCollection = signal<IQuoteItem[]>([]);
  orderAdjustmentTypesSharedCollection = signal<IOrderAdjustmentType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  taxAuthoritiesSharedCollection = signal<ITaxAuthority[]>([]);

  protected quoteAdjustmentService = inject(QuoteAdjustmentService);
  protected quoteAdjustmentFormService = inject(QuoteAdjustmentFormService);
  protected quoteService = inject(QuoteService);
  protected quoteItemService = inject(QuoteItemService);
  protected orderAdjustmentTypeService = inject(OrderAdjustmentTypeService);
  protected productService = inject(ProductService);
  protected productFeatureService = inject(ProductFeatureService);
  protected taxAuthorityService = inject(TaxAuthorityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteAdjustmentFormGroup = this.quoteAdjustmentFormService.createQuoteAdjustmentFormGroup();

  compareQuote = (o1: IQuote | null, o2: IQuote | null): boolean => this.quoteService.compareQuote(o1, o2);

  compareQuoteItem = (o1: IQuoteItem | null, o2: IQuoteItem | null): boolean => this.quoteItemService.compareQuoteItem(o1, o2);

  compareOrderAdjustmentType = (o1: IOrderAdjustmentType | null, o2: IOrderAdjustmentType | null): boolean =>
    this.orderAdjustmentTypeService.compareOrderAdjustmentType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareTaxAuthority = (o1: ITaxAuthority | null, o2: ITaxAuthority | null): boolean =>
    this.taxAuthorityService.compareTaxAuthority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteAdjustment }) => {
      this.quoteAdjustment = quoteAdjustment;
      if (quoteAdjustment) {
        this.updateForm(quoteAdjustment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quoteAdjustment = this.quoteAdjustmentFormService.getQuoteAdjustment(this.editForm);
    if (quoteAdjustment.id === null) {
      this.subscribeToSaveResponse(this.quoteAdjustmentService.create(quoteAdjustment));
    } else {
      this.subscribeToSaveResponse(this.quoteAdjustmentService.update(quoteAdjustment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuoteAdjustment | null>): void {
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

  protected updateForm(quoteAdjustment: IQuoteAdjustment): void {
    this.quoteAdjustment = quoteAdjustment;
    this.quoteAdjustmentFormService.resetForm(this.editForm, quoteAdjustment);

    this.quotesSharedCollection.update(quotes => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, quoteAdjustment.quote));
    this.quoteItemsSharedCollection.update(quoteItems =>
      this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, quoteAdjustment.quoteItem),
    );
    this.orderAdjustmentTypesSharedCollection.update(orderAdjustmentTypes =>
      this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
        orderAdjustmentTypes,
        quoteAdjustment.adjustmentType,
      ),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, quoteAdjustment.product),
    );
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(productFeatures, quoteAdjustment.productFeature),
    );
    this.taxAuthoritiesSharedCollection.update(taxAuthorities =>
      this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(taxAuthorities, quoteAdjustment.taxAuthority),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quoteService
      .query()
      .pipe(map((res: HttpResponse<IQuote[]>) => res.body ?? []))
      .pipe(map((quotes: IQuote[]) => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, this.quoteAdjustment?.quote)))
      .subscribe((quotes: IQuote[]) => this.quotesSharedCollection.set(quotes));

    this.quoteItemService
      .query()
      .pipe(map((res: HttpResponse<IQuoteItem[]>) => res.body ?? []))
      .pipe(
        map((quoteItems: IQuoteItem[]) =>
          this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, this.quoteAdjustment?.quoteItem),
        ),
      )
      .subscribe((quoteItems: IQuoteItem[]) => this.quoteItemsSharedCollection.set(quoteItems));

    this.orderAdjustmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IOrderAdjustmentType[]>) => res.body ?? []))
      .pipe(
        map((orderAdjustmentTypes: IOrderAdjustmentType[]) =>
          this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
            orderAdjustmentTypes,
            this.quoteAdjustment?.adjustmentType,
          ),
        ),
      )
      .subscribe((orderAdjustmentTypes: IOrderAdjustmentType[]) => this.orderAdjustmentTypesSharedCollection.set(orderAdjustmentTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.quoteAdjustment?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.quoteAdjustment?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));

    this.taxAuthorityService
      .query()
      .pipe(map((res: HttpResponse<ITaxAuthority[]>) => res.body ?? []))
      .pipe(
        map((taxAuthorities: ITaxAuthority[]) =>
          this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(taxAuthorities, this.quoteAdjustment?.taxAuthority),
        ),
      )
      .subscribe((taxAuthorities: ITaxAuthority[]) => this.taxAuthoritiesSharedCollection.set(taxAuthorities));
  }
}
