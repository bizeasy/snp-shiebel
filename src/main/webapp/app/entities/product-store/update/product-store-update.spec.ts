import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from '../product-store.model';
import { ProductStoreService } from '../service/product-store.service';

import { ProductStoreFormService } from './product-store-form.service';
import { ProductStoreUpdate } from './product-store-update';

describe('ProductStore Management Update Component', () => {
  let comp: ProductStoreUpdate;
  let fixture: ComponentFixture<ProductStoreUpdate>;
  let activatedRoute: ActivatedRoute;
  let productStoreFormService: ProductStoreFormService;
  let productStoreService: ProductStoreService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(ProductStoreUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productStoreFormService = TestBed.inject(ProductStoreFormService);
    productStoreService = TestBed.inject(ProductStoreService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const productStore: IProductStore = { id: 24662 };
      const owner: IParty = { id: 24527 };
      productStore.owner = owner;
      const admin: IParty = { id: 24527 };
      productStore.admin = admin;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [owner, admin];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStore });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productStore: IProductStore = { id: 24662 };
      const owner: IParty = { id: 24527 };
      productStore.owner = owner;
      const admin: IParty = { id: 24527 };
      productStore.admin = admin;

      activatedRoute.data = of({ productStore });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(owner);
      expect(comp.partiesSharedCollection()).toContainEqual(admin);
      expect(comp.productStore).toEqual(productStore);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductStore>();
      const productStore = { id: 30593 };
      vi.spyOn(productStoreFormService, 'getProductStore').mockReturnValue(productStore);
      vi.spyOn(productStoreService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productStore);
      saveSubject.complete();

      // THEN
      expect(productStoreFormService.getProductStore).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productStoreService.update).toHaveBeenCalledWith(expect.objectContaining(productStore));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductStore>();
      const productStore = { id: 30593 };
      vi.spyOn(productStoreFormService, 'getProductStore').mockReturnValue({ id: null });
      vi.spyOn(productStoreService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStore: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productStore);
      saveSubject.complete();

      // THEN
      expect(productStoreFormService.getProductStore).toHaveBeenCalled();
      expect(productStoreService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductStore>();
      const productStore = { id: 30593 };
      vi.spyOn(productStoreService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productStoreService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
