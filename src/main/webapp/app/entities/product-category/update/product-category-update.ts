import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IProductCategoryType } from 'app/entities/product-category-type/product-category-type.model';
import { ProductCategoryTypeService } from 'app/entities/product-category-type/service/product-category-type.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IProductCategory } from '../product-category.model';
import { ProductCategoryService } from '../service/product-category.service';

import { ProductCategoryFormGroup, ProductCategoryFormService } from './product-category-form.service';

@Component({
  selector: 'sys-product-category-update',
  templateUrl: './product-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  productCategory: IProductCategory | null = null;

  productCategoryTypesSharedCollection = signal<IProductCategoryType[]>([]);
  productCategoriesSharedCollection = signal<IProductCategory[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected productCategoryService = inject(ProductCategoryService);
  protected productCategoryFormService = inject(ProductCategoryFormService);
  protected productCategoryTypeService = inject(ProductCategoryTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductCategoryFormGroup = this.productCategoryFormService.createProductCategoryFormGroup();

  compareProductCategoryType = (o1: IProductCategoryType | null, o2: IProductCategoryType | null): boolean =>
    this.productCategoryTypeService.compareProductCategoryType(o1, o2);

  compareProductCategory = (o1: IProductCategory | null, o2: IProductCategory | null): boolean =>
    this.productCategoryService.compareProductCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCategory }) => {
      this.productCategory = productCategory;
      if (productCategory) {
        this.updateForm(productCategory);
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
    const productCategory = this.productCategoryFormService.getProductCategory(this.editForm);
    if (productCategory.id === null) {
      this.subscribeToSaveResponse(this.productCategoryService.create(productCategory));
    } else {
      this.subscribeToSaveResponse(this.productCategoryService.update(productCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductCategory | null>): void {
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

  protected updateForm(productCategory: IProductCategory): void {
    this.productCategory = productCategory;
    this.productCategoryFormService.resetForm(this.editForm, productCategory);

    this.productCategoryTypesSharedCollection.update(productCategoryTypes =>
      this.productCategoryTypeService.addProductCategoryTypeToCollectionIfMissing<IProductCategoryType>(
        productCategoryTypes,
        productCategory.productCategoryType,
      ),
    );
    this.productCategoriesSharedCollection.update(productCategories =>
      this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(productCategories, productCategory.parent),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productCategoryTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductCategoryType[]>) => res.body ?? []))
      .pipe(
        map((productCategoryTypes: IProductCategoryType[]) =>
          this.productCategoryTypeService.addProductCategoryTypeToCollectionIfMissing<IProductCategoryType>(
            productCategoryTypes,
            this.productCategory?.productCategoryType,
          ),
        ),
      )
      .subscribe((productCategoryTypes: IProductCategoryType[]) => this.productCategoryTypesSharedCollection.set(productCategoryTypes));

    this.productCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductCategory[]>) => res.body ?? []))
      .pipe(
        map((productCategories: IProductCategory[]) =>
          this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
            productCategories,
            this.productCategory?.parent,
          ),
        ),
      )
      .subscribe((productCategories: IProductCategory[]) => this.productCategoriesSharedCollection.set(productCategories));
  }
}
