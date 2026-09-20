import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IKeywordType } from 'app/entities/keyword-type/keyword-type.model';
import { KeywordTypeService } from 'app/entities/keyword-type/service/keyword-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductKeyword } from '../product-keyword.model';
import { ProductKeywordService } from '../service/product-keyword.service';

import { ProductKeywordFormGroup, ProductKeywordFormService } from './product-keyword-form.service';

@Component({
  selector: 'sys-product-keyword-update',
  templateUrl: './product-keyword-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductKeywordUpdate implements OnInit {
  readonly isSaving = signal(false);
  productKeyword: IProductKeyword | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  keywordTypesSharedCollection = signal<IKeywordType[]>([]);

  protected productKeywordService = inject(ProductKeywordService);
  protected productKeywordFormService = inject(ProductKeywordFormService);
  protected productService = inject(ProductService);
  protected keywordTypeService = inject(KeywordTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductKeywordFormGroup = this.productKeywordFormService.createProductKeywordFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareKeywordType = (o1: IKeywordType | null, o2: IKeywordType | null): boolean => this.keywordTypeService.compareKeywordType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productKeyword }) => {
      this.productKeyword = productKeyword;
      if (productKeyword) {
        this.updateForm(productKeyword);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productKeyword = this.productKeywordFormService.getProductKeyword(this.editForm);
    if (productKeyword.id === null) {
      this.subscribeToSaveResponse(this.productKeywordService.create(productKeyword));
    } else {
      this.subscribeToSaveResponse(this.productKeywordService.update(productKeyword));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductKeyword | null>): void {
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

  protected updateForm(productKeyword: IProductKeyword): void {
    this.productKeyword = productKeyword;
    this.productKeywordFormService.resetForm(this.editForm, productKeyword);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productKeyword.product),
    );
    this.keywordTypesSharedCollection.update(keywordTypes =>
      this.keywordTypeService.addKeywordTypeToCollectionIfMissing<IKeywordType>(keywordTypes, productKeyword.keywordType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productKeyword?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.keywordTypeService
      .query()
      .pipe(map((res: HttpResponse<IKeywordType[]>) => res.body ?? []))
      .pipe(
        map((keywordTypes: IKeywordType[]) =>
          this.keywordTypeService.addKeywordTypeToCollectionIfMissing<IKeywordType>(keywordTypes, this.productKeyword?.keywordType),
        ),
      )
      .subscribe((keywordTypes: IKeywordType[]) => this.keywordTypesSharedCollection.set(keywordTypes));
  }
}
