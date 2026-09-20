import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IProductRole } from '../product-role.model';
import { ProductRoleService } from '../service/product-role.service';

import { ProductRoleFormService } from './product-role-form.service';
import { ProductRoleUpdate } from './product-role-update';

describe('ProductRole Management Update Component', () => {
  let comp: ProductRoleUpdate;
  let fixture: ComponentFixture<ProductRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let productRoleFormService: ProductRoleFormService;
  let productRoleService: ProductRoleService;
  let productService: ProductService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ProductRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productRoleFormService = TestBed.inject(ProductRoleFormService);
    productRoleService = TestBed.inject(ProductRoleService);
    productService = TestBed.inject(ProductService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productRole: IProductRole = { id: 10277 };
      const product: IProduct = { id: 21536 };
      productRole.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const productRole: IProductRole = { id: 10277 };
      const party: IParty = { id: 24527 };
      productRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const productRole: IProductRole = { id: 10277 };
      const roleType: IRoleType = { id: 17576 };
      productRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productRole: IProductRole = { id: 10277 };
      const product: IProduct = { id: 21536 };
      productRole.product = product;
      const party: IParty = { id: 24527 };
      productRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      productRole.roleType = roleType;

      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.productRole).toEqual(productRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductRole>();
      const productRole = { id: 7042 };
      vi.spyOn(productRoleFormService, 'getProductRole').mockReturnValue(productRole);
      vi.spyOn(productRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productRole);
      saveSubject.complete();

      // THEN
      expect(productRoleFormService.getProductRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productRoleService.update).toHaveBeenCalledWith(expect.objectContaining(productRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductRole>();
      const productRole = { id: 7042 };
      vi.spyOn(productRoleFormService, 'getProductRole').mockReturnValue({ id: null });
      vi.spyOn(productRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productRole);
      saveSubject.complete();

      // THEN
      expect(productRoleFormService.getProductRole).toHaveBeenCalled();
      expect(productRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductRole>();
      const productRole = { id: 7042 };
      vi.spyOn(productRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
