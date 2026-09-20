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
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductRole } from '../product-role.model';
import { ProductRoleService } from '../service/product-role.service';

import { ProductRoleFormGroup, ProductRoleFormService } from './product-role-form.service';

@Component({
  selector: 'sys-product-role-update',
  templateUrl: './product-role-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductRoleUpdate implements OnInit {
  readonly isSaving = signal(false);
  productRole: IProductRole | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected productRoleService = inject(ProductRoleService);
  protected productRoleFormService = inject(ProductRoleFormService);
  protected productService = inject(ProductService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductRoleFormGroup = this.productRoleFormService.createProductRoleFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productRole }) => {
      this.productRole = productRole;
      if (productRole) {
        this.updateForm(productRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productRole = this.productRoleFormService.getProductRole(this.editForm);
    if (productRole.id === null) {
      this.subscribeToSaveResponse(this.productRoleService.create(productRole));
    } else {
      this.subscribeToSaveResponse(this.productRoleService.update(productRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductRole | null>): void {
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

  protected updateForm(productRole: IProductRole): void {
    this.productRole = productRole;
    this.productRoleFormService.resetForm(this.editForm, productRole);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productRole.product),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, productRole.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, productRole.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productRole?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.productRole?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.productRole?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
