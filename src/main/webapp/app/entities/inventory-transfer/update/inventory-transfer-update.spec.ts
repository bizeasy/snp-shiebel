import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { FacilityLocationService } from 'app/entities/facility-location/service/facility-location.service';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IInventoryTransfer } from '../inventory-transfer.model';
import { InventoryTransferService } from '../service/inventory-transfer.service';

import { InventoryTransferFormService } from './inventory-transfer-form.service';
import { InventoryTransferUpdate } from './inventory-transfer-update';

describe('InventoryTransfer Management Update Component', () => {
  let comp: InventoryTransferUpdate;
  let fixture: ComponentFixture<InventoryTransferUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventoryTransferFormService: InventoryTransferFormService;
  let inventoryTransferService: InventoryTransferService;
  let inventoryItemService: InventoryItemService;
  let statusService: StatusService;
  let facilityService: FacilityService;
  let facilityLocationService: FacilityLocationService;
  let itemIssuanceService: ItemIssuanceService;

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

    fixture = TestBed.createComponent(InventoryTransferUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryTransferFormService = TestBed.inject(InventoryTransferFormService);
    inventoryTransferService = TestBed.inject(InventoryTransferService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    statusService = TestBed.inject(StatusService);
    facilityService = TestBed.inject(FacilityService);
    facilityLocationService = TestBed.inject(FacilityLocationService);
    itemIssuanceService = TestBed.inject(ItemIssuanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InventoryItem query and add missing value', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryTransfer.inventoryItem = inventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [inventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const status: IStatus = { id: 2927 };
      inventoryTransfer.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const facility: IFacility = { id: 4866 };
      inventoryTransfer.facility = facility;
      const facilityTo: IFacility = { id: 4866 };
      inventoryTransfer.facilityTo = facilityTo;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility, facilityTo];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityLocation query and add missing value', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const facilityLocation: IFacilityLocation = { id: 76 };
      inventoryTransfer.facilityLocation = facilityLocation;
      const facilityLocationTo: IFacilityLocation = { id: 76 };
      inventoryTransfer.facilityLocationTo = facilityLocationTo;

      const facilityLocationCollection: IFacilityLocation[] = [{ id: 76 }];
      vi.spyOn(facilityLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityLocationCollection })));
      const additionalFacilityLocations = [facilityLocation, facilityLocationTo];
      const expectedCollection: IFacilityLocation[] = [...additionalFacilityLocations, ...facilityLocationCollection];
      vi.spyOn(facilityLocationService, 'addFacilityLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(facilityLocationService.query).toHaveBeenCalled();
      expect(facilityLocationService.addFacilityLocationToCollectionIfMissing).toHaveBeenCalledWith(
        facilityLocationCollection,
        ...additionalFacilityLocations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityLocationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ItemIssuance query and add missing value', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const issuance: IItemIssuance = { id: 24352 };
      inventoryTransfer.issuance = issuance;

      const itemIssuanceCollection: IItemIssuance[] = [{ id: 24352 }];
      vi.spyOn(itemIssuanceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemIssuanceCollection })));
      const additionalItemIssuances = [issuance];
      const expectedCollection: IItemIssuance[] = [...additionalItemIssuances, ...itemIssuanceCollection];
      vi.spyOn(itemIssuanceService, 'addItemIssuanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(itemIssuanceService.query).toHaveBeenCalled();
      expect(itemIssuanceService.addItemIssuanceToCollectionIfMissing).toHaveBeenCalledWith(
        itemIssuanceCollection,
        ...additionalItemIssuances.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.itemIssuancesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventoryTransfer: IInventoryTransfer = { id: 27132 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryTransfer.inventoryItem = inventoryItem;
      const status: IStatus = { id: 2927 };
      inventoryTransfer.status = status;
      const facility: IFacility = { id: 4866 };
      inventoryTransfer.facility = facility;
      const facilityTo: IFacility = { id: 4866 };
      inventoryTransfer.facilityTo = facilityTo;
      const facilityLocation: IFacilityLocation = { id: 76 };
      inventoryTransfer.facilityLocation = facilityLocation;
      const facilityLocationTo: IFacilityLocation = { id: 76 };
      inventoryTransfer.facilityLocationTo = facilityLocationTo;
      const issuance: IItemIssuance = { id: 24352 };
      inventoryTransfer.issuance = issuance;

      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      expect(comp.inventoryItemsSharedCollection()).toContainEqual(inventoryItem);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facilityTo);
      expect(comp.facilityLocationsSharedCollection()).toContainEqual(facilityLocation);
      expect(comp.facilityLocationsSharedCollection()).toContainEqual(facilityLocationTo);
      expect(comp.itemIssuancesSharedCollection()).toContainEqual(issuance);
      expect(comp.inventoryTransfer).toEqual(inventoryTransfer);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryTransfer>();
      const inventoryTransfer = { id: 25009 };
      vi.spyOn(inventoryTransferFormService, 'getInventoryTransfer').mockReturnValue(inventoryTransfer);
      vi.spyOn(inventoryTransferService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryTransfer);
      saveSubject.complete();

      // THEN
      expect(inventoryTransferFormService.getInventoryTransfer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryTransferService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryTransfer));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryTransfer>();
      const inventoryTransfer = { id: 25009 };
      vi.spyOn(inventoryTransferFormService, 'getInventoryTransfer').mockReturnValue({ id: null });
      vi.spyOn(inventoryTransferService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransfer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryTransfer);
      saveSubject.complete();

      // THEN
      expect(inventoryTransferFormService.getInventoryTransfer).toHaveBeenCalled();
      expect(inventoryTransferService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryTransfer>();
      const inventoryTransfer = { id: 25009 };
      vi.spyOn(inventoryTransferService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryTransferService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInventoryItem', () => {
      it('should forward to inventoryItemService', () => {
        const entity = { id: 7462 };
        const entity2 = { id: 4332 };
        vi.spyOn(inventoryItemService, 'compareInventoryItem');
        comp.compareInventoryItem(entity, entity2);
        expect(inventoryItemService.compareInventoryItem).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacilityLocation', () => {
      it('should forward to facilityLocationService', () => {
        const entity = { id: 76 };
        const entity2 = { id: 28655 };
        vi.spyOn(facilityLocationService, 'compareFacilityLocation');
        comp.compareFacilityLocation(entity, entity2);
        expect(facilityLocationService.compareFacilityLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareItemIssuance', () => {
      it('should forward to itemIssuanceService', () => {
        const entity = { id: 24352 };
        const entity2 = { id: 8405 };
        vi.spyOn(itemIssuanceService, 'compareItemIssuance');
        comp.compareItemIssuance(entity, entity2);
        expect(itemIssuanceService.compareItemIssuance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
