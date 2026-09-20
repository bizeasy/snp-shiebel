import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { ICarrierShipmentMethod } from '../carrier-shipment-method.model';
import { CarrierShipmentMethodService } from '../service/carrier-shipment-method.service';

import { CarrierShipmentMethodFormService } from './carrier-shipment-method-form.service';
import { CarrierShipmentMethodUpdate } from './carrier-shipment-method-update';

describe('CarrierShipmentMethod Management Update Component', () => {
  let comp: CarrierShipmentMethodUpdate;
  let fixture: ComponentFixture<CarrierShipmentMethodUpdate>;
  let activatedRoute: ActivatedRoute;
  let carrierShipmentMethodFormService: CarrierShipmentMethodFormService;
  let carrierShipmentMethodService: CarrierShipmentMethodService;
  let shipmentMethodTypeService: ShipmentMethodTypeService;
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

    fixture = TestBed.createComponent(CarrierShipmentMethodUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carrierShipmentMethodFormService = TestBed.inject(CarrierShipmentMethodFormService);
    carrierShipmentMethodService = TestBed.inject(CarrierShipmentMethodService);
    shipmentMethodTypeService = TestBed.inject(ShipmentMethodTypeService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ShipmentMethodType query and add missing value', () => {
      const carrierShipmentMethod: ICarrierShipmentMethod = { id: 11348 };
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      carrierShipmentMethod.shipmentMethodType = shipmentMethodType;

      const shipmentMethodTypeCollection: IShipmentMethodType[] = [{ id: 6802 }];
      vi.spyOn(shipmentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentMethodTypeCollection })));
      const additionalShipmentMethodTypes = [shipmentMethodType];
      const expectedCollection: IShipmentMethodType[] = [...additionalShipmentMethodTypes, ...shipmentMethodTypeCollection];
      vi.spyOn(shipmentMethodTypeService, 'addShipmentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      expect(shipmentMethodTypeService.query).toHaveBeenCalled();
      expect(shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentMethodTypeCollection,
        ...additionalShipmentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const carrierShipmentMethod: ICarrierShipmentMethod = { id: 11348 };
      const party: IParty = { id: 24527 };
      carrierShipmentMethod.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const carrierShipmentMethod: ICarrierShipmentMethod = { id: 11348 };
      const roleType: IRoleType = { id: 17576 };
      carrierShipmentMethod.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const carrierShipmentMethod: ICarrierShipmentMethod = { id: 11348 };
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      carrierShipmentMethod.shipmentMethodType = shipmentMethodType;
      const party: IParty = { id: 24527 };
      carrierShipmentMethod.party = party;
      const roleType: IRoleType = { id: 17576 };
      carrierShipmentMethod.roleType = roleType;

      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      expect(comp.shipmentMethodTypesSharedCollection()).toContainEqual(shipmentMethodType);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.carrierShipmentMethod).toEqual(carrierShipmentMethod);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICarrierShipmentMethod>();
      const carrierShipmentMethod = { id: 14904 };
      vi.spyOn(carrierShipmentMethodFormService, 'getCarrierShipmentMethod').mockReturnValue(carrierShipmentMethod);
      vi.spyOn(carrierShipmentMethodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(carrierShipmentMethod);
      saveSubject.complete();

      // THEN
      expect(carrierShipmentMethodFormService.getCarrierShipmentMethod).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(carrierShipmentMethodService.update).toHaveBeenCalledWith(expect.objectContaining(carrierShipmentMethod));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICarrierShipmentMethod>();
      const carrierShipmentMethod = { id: 14904 };
      vi.spyOn(carrierShipmentMethodFormService, 'getCarrierShipmentMethod').mockReturnValue({ id: null });
      vi.spyOn(carrierShipmentMethodService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carrierShipmentMethod: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(carrierShipmentMethod);
      saveSubject.complete();

      // THEN
      expect(carrierShipmentMethodFormService.getCarrierShipmentMethod).toHaveBeenCalled();
      expect(carrierShipmentMethodService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICarrierShipmentMethod>();
      const carrierShipmentMethod = { id: 14904 };
      vi.spyOn(carrierShipmentMethodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carrierShipmentMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carrierShipmentMethodService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShipmentMethodType', () => {
      it('should forward to shipmentMethodTypeService', () => {
        const entity = { id: 6802 };
        const entity2 = { id: 25543 };
        vi.spyOn(shipmentMethodTypeService, 'compareShipmentMethodType');
        comp.compareShipmentMethodType(entity, entity2);
        expect(shipmentMethodTypeService.compareShipmentMethodType).toHaveBeenCalledWith(entity, entity2);
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
