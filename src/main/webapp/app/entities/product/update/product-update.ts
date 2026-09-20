import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';
import { TaxSlabService } from 'app/entities/tax-slab/service/tax-slab.service';
import { ITaxSlab } from 'app/entities/tax-slab/tax-slab.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';

import { ProductFormGroup, ProductFormService } from './product-form.service';

@Component({
  selector: 'sys-product-update',
  templateUrl: './product-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductUpdate implements OnInit {
  readonly isSaving = signal(false);
  product: IProduct | null = null;

  productTypesSharedCollection = signal<IProductType[]>([]);
  productCategoriesSharedCollection = signal<IProductCategory[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  inventoryItemTypesSharedCollection = signal<IInventoryItemType[]>([]);
  taxSlabsSharedCollection = signal<ITaxSlab[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected productService = inject(ProductService);
  protected productFormService = inject(ProductFormService);
  protected productTypeService = inject(ProductTypeService);
  protected productCategoryService = inject(ProductCategoryService);
  protected inventoryItemTypeService = inject(InventoryItemTypeService);
  protected taxSlabService = inject(TaxSlabService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  compareProductType = (o1: IProductType | null, o2: IProductType | null): boolean => this.productTypeService.compareProductType(o1, o2);

  compareProductCategory = (o1: IProductCategory | null, o2: IProductCategory | null): boolean =>
    this.productCategoryService.compareProductCategory(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareInventoryItemType = (o1: IInventoryItemType | null, o2: IInventoryItemType | null): boolean =>
    this.inventoryItemTypeService.compareInventoryItemType(o1, o2);

  compareTaxSlab = (o1: ITaxSlab | null, o2: ITaxSlab | null): boolean => this.taxSlabService.compareTaxSlab(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
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
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id === null) {
      this.subscribeToSaveResponse(this.productService.create(product));
    } else {
      this.subscribeToSaveResponse(this.productService.update(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProduct | null>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.productTypesSharedCollection.update(productTypes =>
      this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(productTypes, product.productType),
    );
    this.productCategoriesSharedCollection.update(productCategories =>
      this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
        productCategories,
        product.primaryProductCategory,
      ),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, product.virtualProduct),
    );
    this.inventoryItemTypesSharedCollection.update(inventoryItemTypes =>
      this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
        inventoryItemTypes,
        product.inventoryItemType,
      ),
    );
    this.taxSlabsSharedCollection.update(taxSlabs =>
      this.taxSlabService.addTaxSlabToCollectionIfMissing<ITaxSlab>(taxSlabs, product.taxSlab),
    );
    this.uomsSharedCollection.update(uoms =>
      this.uomService.addUomToCollectionIfMissing<IUom>(uoms, product.uom, product.quantityUom, product.weightUom, product.sizeUom),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductType[]>) => res.body ?? []))
      .pipe(
        map((productTypes: IProductType[]) =>
          this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(productTypes, this.product?.productType),
        ),
      )
      .subscribe((productTypes: IProductType[]) => this.productTypesSharedCollection.set(productTypes));

    this.productCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductCategory[]>) => res.body ?? []))
      .pipe(
        map((productCategories: IProductCategory[]) =>
          this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
            productCategories,
            this.product?.primaryProductCategory,
          ),
        ),
      )
      .subscribe((productCategories: IProductCategory[]) => this.productCategoriesSharedCollection.set(productCategories));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.product?.virtualProduct),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.inventoryItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItemType[]>) => res.body ?? []))
      .pipe(
        map((inventoryItemTypes: IInventoryItemType[]) =>
          this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
            inventoryItemTypes,
            this.product?.inventoryItemType,
          ),
        ),
      )
      .subscribe((inventoryItemTypes: IInventoryItemType[]) => this.inventoryItemTypesSharedCollection.set(inventoryItemTypes));

    this.taxSlabService
      .query()
      .pipe(map((res: HttpResponse<ITaxSlab[]>) => res.body ?? []))
      .pipe(map((taxSlabs: ITaxSlab[]) => this.taxSlabService.addTaxSlabToCollectionIfMissing<ITaxSlab>(taxSlabs, this.product?.taxSlab)))
      .subscribe((taxSlabs: ITaxSlab[]) => this.taxSlabsSharedCollection.set(taxSlabs));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(
        map((uoms: IUom[]) =>
          this.uomService.addUomToCollectionIfMissing<IUom>(
            uoms,
            this.product?.uom,
            this.product?.quantityUom,
            this.product?.weightUom,
            this.product?.sizeUom,
          ),
        ),
      )
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
