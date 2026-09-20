import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductCategoryMember } from '../product-category-member.model';
import { ProductCategoryMemberService } from '../service/product-category-member.service';

import { ProductCategoryMemberFormGroup, ProductCategoryMemberFormService } from './product-category-member-form.service';

@Component({
  selector: 'sys-product-category-member-update',
  templateUrl: './product-category-member-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductCategoryMemberUpdate implements OnInit {
  readonly isSaving = signal(false);
  productCategoryMember: IProductCategoryMember | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  productCategoriesSharedCollection = signal<IProductCategory[]>([]);

  protected productCategoryMemberService = inject(ProductCategoryMemberService);
  protected productCategoryMemberFormService = inject(ProductCategoryMemberFormService);
  protected productService = inject(ProductService);
  protected productCategoryService = inject(ProductCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductCategoryMemberFormGroup = this.productCategoryMemberFormService.createProductCategoryMemberFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductCategory = (o1: IProductCategory | null, o2: IProductCategory | null): boolean =>
    this.productCategoryService.compareProductCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCategoryMember }) => {
      this.productCategoryMember = productCategoryMember;
      if (productCategoryMember) {
        this.updateForm(productCategoryMember);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productCategoryMember = this.productCategoryMemberFormService.getProductCategoryMember(this.editForm);
    if (productCategoryMember.id === null) {
      this.subscribeToSaveResponse(this.productCategoryMemberService.create(productCategoryMember));
    } else {
      this.subscribeToSaveResponse(this.productCategoryMemberService.update(productCategoryMember));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductCategoryMember | null>): void {
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

  protected updateForm(productCategoryMember: IProductCategoryMember): void {
    this.productCategoryMember = productCategoryMember;
    this.productCategoryMemberFormService.resetForm(this.editForm, productCategoryMember);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productCategoryMember.product),
    );
    this.productCategoriesSharedCollection.update(productCategories =>
      this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
        productCategories,
        productCategoryMember.productCategory,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productCategoryMember?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductCategory[]>) => res.body ?? []))
      .pipe(
        map((productCategories: IProductCategory[]) =>
          this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
            productCategories,
            this.productCategoryMember?.productCategory,
          ),
        ),
      )
      .subscribe((productCategories: IProductCategory[]) => this.productCategoriesSharedCollection.set(productCategories));
  }
}
