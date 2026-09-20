import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { OrderAdjustmentTypeService } from 'app/entities/order-adjustment-type/service/order-adjustment-type.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAdjustment } from '../order-adjustment.model';
import { OrderAdjustmentService } from '../service/order-adjustment.service';

import { OrderAdjustmentFormGroup, OrderAdjustmentFormService } from './order-adjustment-form.service';

@Component({
  selector: 'sys-order-adjustment-update',
  templateUrl: './order-adjustment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderAdjustmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderAdjustment: IOrderAdjustment | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  orderAdjustmentTypesSharedCollection = signal<IOrderAdjustmentType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  taxAuthoritiesSharedCollection = signal<ITaxAuthority[]>([]);

  protected orderAdjustmentService = inject(OrderAdjustmentService);
  protected orderAdjustmentFormService = inject(OrderAdjustmentFormService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected orderAdjustmentTypeService = inject(OrderAdjustmentTypeService);
  protected productService = inject(ProductService);
  protected productFeatureService = inject(ProductFeatureService);
  protected taxAuthorityService = inject(TaxAuthorityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderAdjustmentFormGroup = this.orderAdjustmentFormService.createOrderAdjustmentFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareOrderAdjustmentType = (o1: IOrderAdjustmentType | null, o2: IOrderAdjustmentType | null): boolean =>
    this.orderAdjustmentTypeService.compareOrderAdjustmentType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareTaxAuthority = (o1: ITaxAuthority | null, o2: ITaxAuthority | null): boolean =>
    this.taxAuthorityService.compareTaxAuthority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderAdjustment }) => {
      this.orderAdjustment = orderAdjustment;
      if (orderAdjustment) {
        this.updateForm(orderAdjustment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderAdjustment = this.orderAdjustmentFormService.getOrderAdjustment(this.editForm);
    if (orderAdjustment.id === null) {
      this.subscribeToSaveResponse(this.orderAdjustmentService.create(orderAdjustment));
    } else {
      this.subscribeToSaveResponse(this.orderAdjustmentService.update(orderAdjustment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderAdjustment | null>): void {
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

  protected updateForm(orderAdjustment: IOrderAdjustment): void {
    this.orderAdjustment = orderAdjustment;
    this.orderAdjustmentFormService.resetForm(this.editForm, orderAdjustment);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderAdjustment.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, orderAdjustment.orderItem),
    );
    this.orderAdjustmentTypesSharedCollection.update(orderAdjustmentTypes =>
      this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
        orderAdjustmentTypes,
        orderAdjustment.adjustmentType,
      ),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, orderAdjustment.product),
    );
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(productFeatures, orderAdjustment.productFeature),
    );
    this.taxAuthoritiesSharedCollection.update(taxAuthorities =>
      this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(taxAuthorities, orderAdjustment.taxAuthority),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderAdjustment?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.orderAdjustment?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.orderAdjustmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IOrderAdjustmentType[]>) => res.body ?? []))
      .pipe(
        map((orderAdjustmentTypes: IOrderAdjustmentType[]) =>
          this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
            orderAdjustmentTypes,
            this.orderAdjustment?.adjustmentType,
          ),
        ),
      )
      .subscribe((orderAdjustmentTypes: IOrderAdjustmentType[]) => this.orderAdjustmentTypesSharedCollection.set(orderAdjustmentTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.orderAdjustment?.product),
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
            this.orderAdjustment?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));

    this.taxAuthorityService
      .query()
      .pipe(map((res: HttpResponse<ITaxAuthority[]>) => res.body ?? []))
      .pipe(
        map((taxAuthorities: ITaxAuthority[]) =>
          this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(taxAuthorities, this.orderAdjustment?.taxAuthority),
        ),
      )
      .subscribe((taxAuthorities: ITaxAuthority[]) => this.taxAuthoritiesSharedCollection.set(taxAuthorities));
  }
}
