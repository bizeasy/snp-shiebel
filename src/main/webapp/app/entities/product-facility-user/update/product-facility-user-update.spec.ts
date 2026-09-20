import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IProductFacilityUser } from '../product-facility-user.model';
import { ProductFacilityUserService } from '../service/product-facility-user.service';

import { ProductFacilityUserFormService } from './product-facility-user-form.service';
import { ProductFacilityUserUpdate } from './product-facility-user-update';

describe('ProductFacilityUser Management Update Component', () => {
  let comp: ProductFacilityUserUpdate;
  let fixture: ComponentFixture<ProductFacilityUserUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFacilityUserFormService: ProductFacilityUserFormService;
  let productFacilityUserService: ProductFacilityUserService;
  let productFacilityService: ProductFacilityService;
  let partyService: PartyService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(ProductFacilityUserUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFacilityUserFormService = TestBed.inject(ProductFacilityUserFormService);
    productFacilityUserService = TestBed.inject(ProductFacilityUserService);
    productFacilityService = TestBed.inject(ProductFacilityService);
    partyService = TestBed.inject(PartyService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductFacility query and add missing value', () => {
      const productFacilityUser: IProductFacilityUser = { id: 16575 };
      const productFacility: IProductFacility = { id: 30505 };
      productFacilityUser.productFacility = productFacility;

      const productFacilityCollection: IProductFacility[] = [{ id: 30505 }];
      vi.spyOn(productFacilityService, 'query').mockReturnValue(of(new HttpResponse({ body: productFacilityCollection })));
      const additionalProductFacilities = [productFacility];
      const expectedCollection: IProductFacility[] = [...additionalProductFacilities, ...productFacilityCollection];
      vi.spyOn(productFacilityService, 'addProductFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      expect(productFacilityService.query).toHaveBeenCalled();
      expect(productFacilityService.addProductFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        productFacilityCollection,
        ...additionalProductFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFacilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const productFacilityUser: IProductFacilityUser = { id: 16575 };
      const party: IParty = { id: 24527 };
      productFacilityUser.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const productFacilityUser: IProductFacilityUser = { id: 16575 };
      const status: IStatus = { id: 2927 };
      productFacilityUser.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFacilityUser: IProductFacilityUser = { id: 16575 };
      const productFacility: IProductFacility = { id: 30505 };
      productFacilityUser.productFacility = productFacility;
      const party: IParty = { id: 24527 };
      productFacilityUser.party = party;
      const status: IStatus = { id: 2927 };
      productFacilityUser.status = status;

      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      expect(comp.productFacilitiesSharedCollection()).toContainEqual(productFacility);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.productFacilityUser).toEqual(productFacilityUser);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityUser>();
      const productFacilityUser = { id: 234 };
      vi.spyOn(productFacilityUserFormService, 'getProductFacilityUser').mockReturnValue(productFacilityUser);
      vi.spyOn(productFacilityUserService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacilityUser);
      saveSubject.complete();

      // THEN
      expect(productFacilityUserFormService.getProductFacilityUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFacilityUserService.update).toHaveBeenCalledWith(expect.objectContaining(productFacilityUser));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityUser>();
      const productFacilityUser = { id: 234 };
      vi.spyOn(productFacilityUserFormService, 'getProductFacilityUser').mockReturnValue({ id: null });
      vi.spyOn(productFacilityUserService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacilityUser);
      saveSubject.complete();

      // THEN
      expect(productFacilityUserFormService.getProductFacilityUser).toHaveBeenCalled();
      expect(productFacilityUserService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityUser>();
      const productFacilityUser = { id: 234 };
      vi.spyOn(productFacilityUserService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFacilityUserService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductFacility', () => {
      it('should forward to productFacilityService', () => {
        const entity = { id: 30505 };
        const entity2 = { id: 4242 };
        vi.spyOn(productFacilityService, 'compareProductFacility');
        comp.compareProductFacility(entity, entity2);
        expect(productFacilityService.compareProductFacility).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
