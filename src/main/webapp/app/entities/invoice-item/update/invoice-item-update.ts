import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';

import { InvoiceItemFormGroup, InvoiceItemFormService } from './invoice-item-form.service';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { InvoiceItemTypeService } from 'app/entities/invoice-item-type/service/invoice-item-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { TaxAuthorityRateTypeService } from 'app/entities/tax-authority-rate-type/service/tax-authority-rate-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';

@Component({
  selector: 'sys-invoice-item-update',
  templateUrl: './invoice-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoiceItem: IInvoiceItem | null = null;

  invoicesSharedCollection = signal<IInvoice[]>([]);
  invoiceItemTypesSharedCollection = signal<IInvoiceItemType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  productFacilitiesSharedCollection = signal<IProductFacility[]>([]);
  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  taxAuthorityRateTypesSharedCollection = signal<ITaxAuthorityRateType[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);

  protected invoiceItemService = inject(InvoiceItemService);
  protected invoiceItemFormService = inject(InvoiceItemFormService);
  protected invoiceService = inject(InvoiceService);
  protected invoiceItemTypeService = inject(InvoiceItemTypeService);
  protected productService = inject(ProductService);
  protected uomService = inject(UomService);
  protected productFeatureService = inject(ProductFeatureService);
  protected productFacilityService = inject(ProductFacilityService);
  protected inventoryItemService = inject(InventoryItemService);
  protected taxAuthorityRateTypeService = inject(TaxAuthorityRateTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceItemFormGroup = this.invoiceItemFormService.createInvoiceItemFormGroup();

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  compareInvoiceItemType = (o1: IInvoiceItemType | null, o2: IInvoiceItemType | null): boolean =>
    this.invoiceItemTypeService.compareInvoiceItemType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareProductFacility = (o1: IProductFacility | null, o2: IProductFacility | null): boolean =>
    this.productFacilityService.compareProductFacility(o1, o2);

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareTaxAuthorityRateType = (o1: ITaxAuthorityRateType | null, o2: ITaxAuthorityRateType | null): boolean =>
    this.taxAuthorityRateTypeService.compareTaxAuthorityRateType(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItem }) => {
      this.invoiceItem = invoiceItem;
      if (invoiceItem) {
        this.updateForm(invoiceItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoiceItem = this.invoiceItemFormService.getInvoiceItem(this.editForm);
    if (invoiceItem.id === null) {
      this.subscribeToSaveResponse(this.invoiceItemService.create(invoiceItem));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemService.update(invoiceItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoiceItem | null>): void {
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

  protected updateForm(invoiceItem: IInvoiceItem): void {
    this.invoiceItem = invoiceItem;
    this.invoiceItemFormService.resetForm(this.editForm, invoiceItem);

    this.invoicesSharedCollection.update(invoices =>
      this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, invoiceItem.invoice, invoiceItem.parentInvoice),
    );
    this.invoiceItemTypesSharedCollection.update(invoiceItemTypes =>
      this.invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing<IInvoiceItemType>(invoiceItemTypes, invoiceItem.invoiceItemType),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, invoiceItem.product),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, invoiceItem.uom));
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(productFeatures, invoiceItem.productFeature),
    );
    this.productFacilitiesSharedCollection.update(productFacilities =>
      this.productFacilityService.addProductFacilityToCollectionIfMissing<IProductFacility>(productFacilities, invoiceItem.productFacility),
    );
    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, invoiceItem.fromInventoryItem),
    );
    this.taxAuthorityRateTypesSharedCollection.update(taxAuthorityRateTypes =>
      this.taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing<ITaxAuthorityRateType>(
        taxAuthorityRateTypes,
        invoiceItem.taxAuthRate,
      ),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, invoiceItem.parentInvoiceItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) =>
          this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
            invoices,
            this.invoiceItem?.invoice,
            this.invoiceItem?.parentInvoice,
          ),
        ),
      )
      .subscribe((invoices: IInvoice[]) => this.invoicesSharedCollection.set(invoices));

    this.invoiceItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItemType[]>) => res.body ?? []))
      .pipe(
        map((invoiceItemTypes: IInvoiceItemType[]) =>
          this.invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing<IInvoiceItemType>(
            invoiceItemTypes,
            this.invoiceItem?.invoiceItemType,
          ),
        ),
      )
      .subscribe((invoiceItemTypes: IInvoiceItemType[]) => this.invoiceItemTypesSharedCollection.set(invoiceItemTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.invoiceItem?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.invoiceItem?.uom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.invoiceItem?.productFeature,
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
            this.invoiceItem?.productFacility,
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
            this.invoiceItem?.fromInventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.taxAuthorityRateTypeService
      .query()
      .pipe(map((res: HttpResponse<ITaxAuthorityRateType[]>) => res.body ?? []))
      .pipe(
        map((taxAuthorityRateTypes: ITaxAuthorityRateType[]) =>
          this.taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing<ITaxAuthorityRateType>(
            taxAuthorityRateTypes,
            this.invoiceItem?.taxAuthRate,
          ),
        ),
      )
      .subscribe((taxAuthorityRateTypes: ITaxAuthorityRateType[]) => this.taxAuthorityRateTypesSharedCollection.set(taxAuthorityRateTypes));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.invoiceItem?.parentInvoiceItem),
        ),
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => this.invoiceItemsSharedCollection.set(invoiceItems));
  }
}
