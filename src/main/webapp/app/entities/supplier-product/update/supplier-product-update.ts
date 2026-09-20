import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { SupplierProductService } from '../service/supplier-product.service';
import { ISupplierProduct } from '../supplier-product.model';

import { SupplierProductFormGroup, SupplierProductFormService } from './supplier-product-form.service';

@Component({
  selector: 'sys-supplier-product-update',
  templateUrl: './supplier-product-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SupplierProductUpdate implements OnInit {
  readonly isSaving = signal(false);
  supplierProduct: ISupplierProduct | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected supplierProductService = inject(SupplierProductService);
  protected supplierProductFormService = inject(SupplierProductFormService);
  protected productService = inject(ProductService);
  protected partyService = inject(PartyService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SupplierProductFormGroup = this.supplierProductFormService.createSupplierProductFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierProduct }) => {
      this.supplierProduct = supplierProduct;
      if (supplierProduct) {
        this.updateForm(supplierProduct);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const supplierProduct = this.supplierProductFormService.getSupplierProduct(this.editForm);
    if (supplierProduct.id === null) {
      this.subscribeToSaveResponse(this.supplierProductService.create(supplierProduct));
    } else {
      this.subscribeToSaveResponse(this.supplierProductService.update(supplierProduct));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISupplierProduct | null>): void {
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

  protected updateForm(supplierProduct: ISupplierProduct): void {
    this.supplierProduct = supplierProduct;
    this.supplierProductFormService.resetForm(this.editForm, supplierProduct);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, supplierProduct.product),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, supplierProduct.party));
    this.uomsSharedCollection.update(uoms =>
      this.uomService.addUomToCollectionIfMissing<IUom>(uoms, supplierProduct.quantityUom, supplierProduct.currencyUom),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.supplierProduct?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.supplierProduct?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(
        map((uoms: IUom[]) =>
          this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.supplierProduct?.quantityUom, this.supplierProduct?.currencyUom),
        ),
      )
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
