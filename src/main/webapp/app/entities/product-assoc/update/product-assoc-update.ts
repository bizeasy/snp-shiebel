import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductAssocType } from 'app/entities/product-assoc-type/product-assoc-type.model';
import { ProductAssocTypeService } from 'app/entities/product-assoc-type/service/product-assoc-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductAssoc } from '../product-assoc.model';
import { ProductAssocService } from '../service/product-assoc.service';

import { ProductAssocFormGroup, ProductAssocFormService } from './product-assoc-form.service';

@Component({
  selector: 'sys-product-assoc-update',
  templateUrl: './product-assoc-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductAssocUpdate implements OnInit {
  readonly isSaving = signal(false);
  productAssoc: IProductAssoc | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  productAssocTypesSharedCollection = signal<IProductAssocType[]>([]);

  protected productAssocService = inject(ProductAssocService);
  protected productAssocFormService = inject(ProductAssocFormService);
  protected productService = inject(ProductService);
  protected productAssocTypeService = inject(ProductAssocTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductAssocFormGroup = this.productAssocFormService.createProductAssocFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductAssocType = (o1: IProductAssocType | null, o2: IProductAssocType | null): boolean =>
    this.productAssocTypeService.compareProductAssocType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productAssoc }) => {
      this.productAssoc = productAssoc;
      if (productAssoc) {
        this.updateForm(productAssoc);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productAssoc = this.productAssocFormService.getProductAssoc(this.editForm);
    if (productAssoc.id === null) {
      this.subscribeToSaveResponse(this.productAssocService.create(productAssoc));
    } else {
      this.subscribeToSaveResponse(this.productAssocService.update(productAssoc));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductAssoc | null>): void {
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

  protected updateForm(productAssoc: IProductAssoc): void {
    this.productAssoc = productAssoc;
    this.productAssocFormService.resetForm(this.editForm, productAssoc);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productAssoc.product, productAssoc.productTo),
    );
    this.productAssocTypesSharedCollection.update(productAssocTypes =>
      this.productAssocTypeService.addProductAssocTypeToCollectionIfMissing<IProductAssocType>(
        productAssocTypes,
        productAssoc.productAssocType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productAssoc?.product, this.productAssoc?.productTo),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productAssocTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductAssocType[]>) => res.body ?? []))
      .pipe(
        map((productAssocTypes: IProductAssocType[]) =>
          this.productAssocTypeService.addProductAssocTypeToCollectionIfMissing<IProductAssocType>(
            productAssocTypes,
            this.productAssoc?.productAssocType,
          ),
        ),
      )
      .subscribe((productAssocTypes: IProductAssocType[]) => this.productAssocTypesSharedCollection.set(productAssocTypes));
  }
}
