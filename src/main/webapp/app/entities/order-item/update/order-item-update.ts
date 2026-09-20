import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItemType } from 'app/entities/order-item-type/order-item-type.model';
import { OrderItemTypeService } from 'app/entities/order-item-type/service/order-item-type.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { ISupplierProduct } from 'app/entities/supplier-product/supplier-product.model';
import { SupplierProductService } from 'app/entities/supplier-product/service/supplier-product.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { ShoppingListItemService } from 'app/entities/shopping-list-item/service/shopping-list-item.service';
import { IShoppingListItem } from 'app/entities/shopping-list-item/shopping-list-item.model';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { TaxAuthorityRateTypeService } from 'app/entities/tax-authority-rate-type/service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';

import { OrderItemFormGroup, OrderItemFormService } from './order-item-form.service';

@Component({
  selector: 'sys-order-item-update',
  templateUrl: './order-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderItem: IOrderItem | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemTypesSharedCollection = signal<IOrderItemType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  supplierProductsSharedCollection = signal<ISupplierProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  productFacilitiesSharedCollection = signal<IProductFacility[]>([]);
  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  shoppingListItemsSharedCollection = signal<IShoppingListItem[]>([]);
  quoteItemsSharedCollection = signal<IQuoteItem[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  taxAuthorityRateTypesSharedCollection = signal<ITaxAuthorityRateType[]>([]);

  protected orderItemService = inject(OrderItemService);
  protected orderItemFormService = inject(OrderItemFormService);
  protected orderService = inject(OrderService);
  protected orderItemTypeService = inject(OrderItemTypeService);
  protected productService = inject(ProductService);
  protected uomService = inject(UomService);
  protected supplierProductService = inject(SupplierProductService);
  protected partyService = inject(PartyService);
  protected productFeatureService = inject(ProductFeatureService);
  protected productFacilityService = inject(ProductFacilityService);
  protected inventoryItemService = inject(InventoryItemService);
  protected shoppingListItemService = inject(ShoppingListItemService);
  protected quoteItemService = inject(QuoteItemService);
  protected statusService = inject(StatusService);
  protected taxAuthorityRateTypeService = inject(TaxAuthorityRateTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderItemFormGroup = this.orderItemFormService.createOrderItemFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItemType = (o1: IOrderItemType | null, o2: IOrderItemType | null): boolean =>
    this.orderItemTypeService.compareOrderItemType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareSupplierProduct = (o1: ISupplierProduct | null, o2: ISupplierProduct | null): boolean =>
    this.supplierProductService.compareSupplierProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareProductFacility = (o1: IProductFacility | null, o2: IProductFacility | null): boolean =>
    this.productFacilityService.compareProductFacility(o1, o2);

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareShoppingListItem = (o1: IShoppingListItem | null, o2: IShoppingListItem | null): boolean =>
    this.shoppingListItemService.compareShoppingListItem(o1, o2);

  compareQuoteItem = (o1: IQuoteItem | null, o2: IQuoteItem | null): boolean => this.quoteItemService.compareQuoteItem(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareTaxAuthorityRateType = (o1: ITaxAuthorityRateType | null, o2: ITaxAuthorityRateType | null): boolean =>
    this.taxAuthorityRateTypeService.compareTaxAuthorityRateType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItem }) => {
      this.orderItem = orderItem;
      if (orderItem) {
        this.updateForm(orderItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderItem = this.orderItemFormService.getOrderItem(this.editForm);
    if (orderItem.id === null) {
      this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    } else {
      this.subscribeToSaveResponse(this.orderItemService.update(orderItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderItem | null>): void {
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

  protected updateForm(orderItem: IOrderItem): void {
    this.orderItem = orderItem;
    this.orderItemFormService.resetForm(this.editForm, orderItem);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderItem.order));
    this.orderItemTypesSharedCollection.update(orderItemTypes =>
      this.orderItemTypeService.addOrderItemTypeToCollectionIfMissing<IOrderItemType>(orderItemTypes, orderItem.orderItemType),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, orderItem.product),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, orderItem.uom));
    this.supplierProductsSharedCollection.update(supplierProducts =>
      this.supplierProductService.addSupplierProductToCollectionIfMissing<ISupplierProduct>(supplierProducts, orderItem.supplierProduct),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, orderItem.supplier));
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(productFeatures, orderItem.productFeature),
    );
    this.productFacilitiesSharedCollection.update(productFacilities =>
      this.productFacilityService.addProductFacilityToCollectionIfMissing<IProductFacility>(productFacilities, orderItem.productFacility),
    );
    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, orderItem.fromInventoryItem),
    );
    this.shoppingListItemsSharedCollection.update(shoppingListItems =>
      this.shoppingListItemService.addShoppingListItemToCollectionIfMissing<IShoppingListItem>(
        shoppingListItems,
        orderItem.shoppingListItem,
      ),
    );
    this.quoteItemsSharedCollection.update(quoteItems =>
      this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, orderItem.quoteItem),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, orderItem.status),
    );
    this.taxAuthorityRateTypesSharedCollection.update(taxAuthorityRateTypes =>
      this.taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing<ITaxAuthorityRateType>(
        taxAuthorityRateTypes,
        orderItem.taxAuthRate,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderItem?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IOrderItemType[]>) => res.body ?? []))
      .pipe(
        map((orderItemTypes: IOrderItemType[]) =>
          this.orderItemTypeService.addOrderItemTypeToCollectionIfMissing<IOrderItemType>(orderItemTypes, this.orderItem?.orderItemType),
        ),
      )
      .subscribe((orderItemTypes: IOrderItemType[]) => this.orderItemTypesSharedCollection.set(orderItemTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.orderItem?.product)))
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.orderItem?.uom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.supplierProductService
      .query()
      .pipe(map((res: HttpResponse<ISupplierProduct[]>) => res.body ?? []))
      .pipe(
        map((supplierProducts: ISupplierProduct[]) =>
          this.supplierProductService.addSupplierProductToCollectionIfMissing<ISupplierProduct>(
            supplierProducts,
            this.orderItem?.supplierProduct,
          ),
        ),
      )
      .subscribe((supplierProducts: ISupplierProduct[]) => this.supplierProductsSharedCollection.set(supplierProducts));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.orderItem?.supplier)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.orderItem?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));

    this.productFacilityService
      .query()
      .pipe(map((res: HttpResponse<IProductFacility[]>) => res.body ?? []))
      .pipe(
        map((productFacilities: IProductFacility[]) =>
          this.productFacilityService.addProductFacilityToCollectionIfMissing<IProductFacility>(
            productFacilities,
            this.orderItem?.productFacility,
          ),
        ),
      )
      .subscribe((productFacilities: IProductFacility[]) => this.productFacilitiesSharedCollection.set(productFacilities));

    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(
            inventoryItems,
            this.orderItem?.fromInventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.shoppingListItemService
      .query()
      .pipe(map((res: HttpResponse<IShoppingListItem[]>) => res.body ?? []))
      .pipe(
        map((shoppingListItems: IShoppingListItem[]) =>
          this.shoppingListItemService.addShoppingListItemToCollectionIfMissing<IShoppingListItem>(
            shoppingListItems,
            this.orderItem?.shoppingListItem,
          ),
        ),
      )
      .subscribe((shoppingListItems: IShoppingListItem[]) => this.shoppingListItemsSharedCollection.set(shoppingListItems));

    this.quoteItemService
      .query()
      .pipe(map((res: HttpResponse<IQuoteItem[]>) => res.body ?? []))
      .pipe(
        map((quoteItems: IQuoteItem[]) =>
          this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, this.orderItem?.quoteItem),
        ),
      )
      .subscribe((quoteItems: IQuoteItem[]) => this.quoteItemsSharedCollection.set(quoteItems));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.orderItem?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.taxAuthorityRateTypeService
      .query()
      .pipe(map((res: HttpResponse<ITaxAuthorityRateType[]>) => res.body ?? []))
      .pipe(
        map((taxAuthorityRateTypes: ITaxAuthorityRateType[]) =>
          this.taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing<ITaxAuthorityRateType>(
            taxAuthorityRateTypes,
            this.orderItem?.taxAuthRate,
          ),
        ),
      )
      .subscribe((taxAuthorityRateTypes: ITaxAuthorityRateType[]) => this.taxAuthorityRateTypesSharedCollection.set(taxAuthorityRateTypes));
  }
}
