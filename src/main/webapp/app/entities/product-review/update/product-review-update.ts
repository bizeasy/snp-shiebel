import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IProductReview } from '../product-review.model';
import { ProductReviewService } from '../service/product-review.service';

import { ProductReviewFormGroup, ProductReviewFormService } from './product-review-form.service';
import { type BlobType } from 'app/shared/jhipster/data-utils';

@Component({
  selector: 'sys-product-review-update',
  templateUrl: './product-review-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductReviewUpdate implements OnInit {
  readonly isSaving = signal(false);
  productReview: IProductReview | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected productReviewService = inject(ProductReviewService);
  protected productReviewFormService = inject(ProductReviewFormService);
  protected productService = inject(ProductService);
  protected partyService = inject(PartyService);
  protected orderItemService = inject(OrderItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductReviewFormGroup = this.productReviewFormService.createProductReviewFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productReview }) => {
      this.productReview = productReview;
      if (productReview) {
        this.updateForm(productReview);
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
    const productReview = this.productReviewFormService.getProductReview(this.editForm);
    if (productReview.id === null) {
      this.subscribeToSaveResponse(this.productReviewService.create(productReview));
    } else {
      this.subscribeToSaveResponse(this.productReviewService.update(productReview));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductReview | null>): void {
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

  protected updateForm(productReview: IProductReview): void {
    this.productReview = productReview;
    this.productReviewFormService.resetForm(this.editForm, productReview);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productReview.product),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, productReview.party));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, productReview.orderItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productReview?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.productReview?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.productReview?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));
  }
}
