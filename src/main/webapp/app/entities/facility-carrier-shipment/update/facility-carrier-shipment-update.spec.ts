import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { IFacilityCarrierShipment } from '../facility-carrier-shipment.model';
import { FacilityCarrierShipmentService } from '../service/facility-carrier-shipment.service';

import { FacilityCarrierShipmentFormService } from './facility-carrier-shipment-form.service';
import { FacilityCarrierShipmentUpdate } from './facility-carrier-shipment-update';

describe('FacilityCarrierShipment Management Update Component', () => {
  let comp: FacilityCarrierShipmentUpdate;
  let fixture: ComponentFixture<FacilityCarrierShipmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityCarrierShipmentFormService: FacilityCarrierShipmentFormService;
  let facilityCarrierShipmentService: FacilityCarrierShipmentService;
  let facilityService: FacilityService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;
  let shipmentMethodTypeService: ShipmentMethodTypeService;

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

    fixture = TestBed.createComponent(FacilityCarrierShipmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityCarrierShipmentFormService = TestBed.inject(FacilityCarrierShipmentFormService);
    facilityCarrierShipmentService = TestBed.inject(FacilityCarrierShipmentService);
    facilityService = TestBed.inject(FacilityService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);
    shipmentMethodTypeService = TestBed.inject(ShipmentMethodTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facility query and add missing value', () => {
      const facilityCarrierShipment: IFacilityCarrierShipment = { id: 31799 };
      const facility: IFacility = { id: 4866 };
      facilityCarrierShipment.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const facilityCarrierShipment: IFacilityCarrierShipment = { id: 31799 };
      const party: IParty = { id: 24527 };
      facilityCarrierShipment.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const facilityCarrierShipment: IFacilityCarrierShipment = { id: 31799 };
      const roleType: IRoleType = { id: 17576 };
      facilityCarrierShipment.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentMethodType query and add missing value', () => {
      const facilityCarrierShipment: IFacilityCarrierShipment = { id: 31799 };
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      facilityCarrierShipment.shipmentMethodType = shipmentMethodType;

      const shipmentMethodTypeCollection: IShipmentMethodType[] = [{ id: 6802 }];
      vi.spyOn(shipmentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentMethodTypeCollection })));
      const additionalShipmentMethodTypes = [shipmentMethodType];
      const expectedCollection: IShipmentMethodType[] = [...additionalShipmentMethodTypes, ...shipmentMethodTypeCollection];
      vi.spyOn(shipmentMethodTypeService, 'addShipmentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      expect(shipmentMethodTypeService.query).toHaveBeenCalled();
      expect(shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentMethodTypeCollection,
        ...additionalShipmentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityCarrierShipment: IFacilityCarrierShipment = { id: 31799 };
      const facility: IFacility = { id: 4866 };
      facilityCarrierShipment.facility = facility;
      const party: IParty = { id: 24527 };
      facilityCarrierShipment.party = party;
      const roleType: IRoleType = { id: 17576 };
      facilityCarrierShipment.roleType = roleType;
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      facilityCarrierShipment.shipmentMethodType = shipmentMethodType;

      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.shipmentMethodTypesSharedCollection()).toContainEqual(shipmentMethodType);
      expect(comp.facilityCarrierShipment).toEqual(facilityCarrierShipment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityCarrierShipment>();
      const facilityCarrierShipment = { id: 12764 };
      vi.spyOn(facilityCarrierShipmentFormService, 'getFacilityCarrierShipment').mockReturnValue(facilityCarrierShipment);
      vi.spyOn(facilityCarrierShipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityCarrierShipment);
      saveSubject.complete();

      // THEN
      expect(facilityCarrierShipmentFormService.getFacilityCarrierShipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityCarrierShipmentService.update).toHaveBeenCalledWith(expect.objectContaining(facilityCarrierShipment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityCarrierShipment>();
      const facilityCarrierShipment = { id: 12764 };
      vi.spyOn(facilityCarrierShipmentFormService, 'getFacilityCarrierShipment').mockReturnValue({ id: null });
      vi.spyOn(facilityCarrierShipmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityCarrierShipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityCarrierShipment);
      saveSubject.complete();

      // THEN
      expect(facilityCarrierShipmentFormService.getFacilityCarrierShipment).toHaveBeenCalled();
      expect(facilityCarrierShipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityCarrierShipment>();
      const facilityCarrierShipment = { id: 12764 };
      vi.spyOn(facilityCarrierShipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityCarrierShipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityCarrierShipmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareShipmentMethodType', () => {
      it('should forward to shipmentMethodTypeService', () => {
        const entity = { id: 6802 };
        const entity2 = { id: 25543 };
        vi.spyOn(shipmentMethodTypeService, 'compareShipmentMethodType');
        comp.compareShipmentMethodType(entity, entity2);
        expect(shipmentMethodTypeService.compareShipmentMethodType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
