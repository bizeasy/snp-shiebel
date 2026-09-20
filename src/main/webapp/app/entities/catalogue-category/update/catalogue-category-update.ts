import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICatalogue } from 'app/entities/catalogue/catalogue.model';
import { CatalogueService } from 'app/entities/catalogue/service/catalogue.service';
import { ICatalogueCategoryType } from 'app/entities/catalogue-category-type/catalogue-category-type.model';
import { CatalogueCategoryTypeService } from 'app/entities/catalogue-category-type/service/catalogue-category-type.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogueCategory } from '../catalogue-category.model';
import { CatalogueCategoryService } from '../service/catalogue-category.service';

import { CatalogueCategoryFormGroup, CatalogueCategoryFormService } from './catalogue-category-form.service';

@Component({
  selector: 'sys-catalogue-category-update',
  templateUrl: './catalogue-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CatalogueCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  catalogueCategory: ICatalogueCategory | null = null;

  cataloguesSharedCollection = signal<ICatalogue[]>([]);
  productCategoriesSharedCollection = signal<IProductCategory[]>([]);
  catalogueCategoryTypesSharedCollection = signal<ICatalogueCategoryType[]>([]);

  protected catalogueCategoryService = inject(CatalogueCategoryService);
  protected catalogueCategoryFormService = inject(CatalogueCategoryFormService);
  protected catalogueService = inject(CatalogueService);
  protected productCategoryService = inject(ProductCategoryService);
  protected catalogueCategoryTypeService = inject(CatalogueCategoryTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CatalogueCategoryFormGroup = this.catalogueCategoryFormService.createCatalogueCategoryFormGroup();

  compareCatalogue = (o1: ICatalogue | null, o2: ICatalogue | null): boolean => this.catalogueService.compareCatalogue(o1, o2);

  compareProductCategory = (o1: IProductCategory | null, o2: IProductCategory | null): boolean =>
    this.productCategoryService.compareProductCategory(o1, o2);

  compareCatalogueCategoryType = (o1: ICatalogueCategoryType | null, o2: ICatalogueCategoryType | null): boolean =>
    this.catalogueCategoryTypeService.compareCatalogueCategoryType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogueCategory }) => {
      this.catalogueCategory = catalogueCategory;
      if (catalogueCategory) {
        this.updateForm(catalogueCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const catalogueCategory = this.catalogueCategoryFormService.getCatalogueCategory(this.editForm);
    if (catalogueCategory.id === null) {
      this.subscribeToSaveResponse(this.catalogueCategoryService.create(catalogueCategory));
    } else {
      this.subscribeToSaveResponse(this.catalogueCategoryService.update(catalogueCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICatalogueCategory | null>): void {
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

  protected updateForm(catalogueCategory: ICatalogueCategory): void {
    this.catalogueCategory = catalogueCategory;
    this.catalogueCategoryFormService.resetForm(this.editForm, catalogueCategory);

    this.cataloguesSharedCollection.update(catalogues =>
      this.catalogueService.addCatalogueToCollectionIfMissing<ICatalogue>(catalogues, catalogueCategory.catalogue),
    );
    this.productCategoriesSharedCollection.update(productCategories =>
      this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
        productCategories,
        catalogueCategory.productCategory,
      ),
    );
    this.catalogueCategoryTypesSharedCollection.update(catalogueCategoryTypes =>
      this.catalogueCategoryTypeService.addCatalogueCategoryTypeToCollectionIfMissing<ICatalogueCategoryType>(
        catalogueCategoryTypes,
        catalogueCategory.catalogueCategoryType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogueService
      .query()
      .pipe(map((res: HttpResponse<ICatalogue[]>) => res.body ?? []))
      .pipe(
        map((catalogues: ICatalogue[]) =>
          this.catalogueService.addCatalogueToCollectionIfMissing<ICatalogue>(catalogues, this.catalogueCategory?.catalogue),
        ),
      )
      .subscribe((catalogues: ICatalogue[]) => this.cataloguesSharedCollection.set(catalogues));

    this.productCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductCategory[]>) => res.body ?? []))
      .pipe(
        map((productCategories: IProductCategory[]) =>
          this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
            productCategories,
            this.catalogueCategory?.productCategory,
          ),
        ),
      )
      .subscribe((productCategories: IProductCategory[]) => this.productCategoriesSharedCollection.set(productCategories));

    this.catalogueCategoryTypeService
      .query()
      .pipe(map((res: HttpResponse<ICatalogueCategoryType[]>) => res.body ?? []))
      .pipe(
        map((catalogueCategoryTypes: ICatalogueCategoryType[]) =>
          this.catalogueCategoryTypeService.addCatalogueCategoryTypeToCollectionIfMissing<ICatalogueCategoryType>(
            catalogueCategoryTypes,
            this.catalogueCategory?.catalogueCategoryType,
          ),
        ),
      )
      .subscribe((catalogueCategoryTypes: ICatalogueCategoryType[]) =>
        this.catalogueCategoryTypesSharedCollection.set(catalogueCategoryTypes),
      );
  }
}
