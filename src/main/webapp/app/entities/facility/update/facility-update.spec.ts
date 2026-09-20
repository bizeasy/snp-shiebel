import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';
import { FacilityGroupService } from 'app/entities/facility-group/service/facility-group.service';
import { IFacilityType } from 'app/entities/facility-type/facility-type.model';
import { FacilityTypeService } from 'app/entities/facility-type/service/facility-type.service';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';
import { GeoPointService } from 'app/entities/geo-point/service/geo-point.service';
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IFacility } from '../facility.model';
import { FacilityService } from '../service/facility.service';

import { FacilityFormService } from './facility-form.service';
import { FacilityUpdate } from './facility-update';

describe('Facility Management Update Component', () => {
  let comp: FacilityUpdate;
  let fixture: ComponentFixture<FacilityUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityFormService: FacilityFormService;
  let facilityService: FacilityService;
  let facilityTypeService: FacilityTypeService;
  let partyService: PartyService;
  let facilityGroupService: FacilityGroupService;
  let uomService: UomService;
  let geoPointService: GeoPointService;
  let inventoryItemTypeService: InventoryItemTypeService;
  let productStoreService: ProductStoreService;

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

    fixture = TestBed.createComponent(FacilityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityFormService = TestBed.inject(FacilityFormService);
    facilityService = TestBed.inject(FacilityService);
    facilityTypeService = TestBed.inject(FacilityTypeService);
    partyService = TestBed.inject(PartyService);
    facilityGroupService = TestBed.inject(FacilityGroupService);
    uomService = TestBed.inject(UomService);
    geoPointService = TestBed.inject(GeoPointService);
    inventoryItemTypeService = TestBed.inject(InventoryItemTypeService);
    productStoreService = TestBed.inject(ProductStoreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FacilityType query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const facilityType: IFacilityType = { id: 18255 };
      facility.facilityType = facilityType;

      const facilityTypeCollection: IFacilityType[] = [{ id: 18255 }];
      vi.spyOn(facilityTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityTypeCollection })));
      const additionalFacilityTypes = [facilityType];
      const expectedCollection: IFacilityType[] = [...additionalFacilityTypes, ...facilityTypeCollection];
      vi.spyOn(facilityTypeService, 'addFacilityTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(facilityTypeService.query).toHaveBeenCalled();
      expect(facilityTypeService.addFacilityTypeToCollectionIfMissing).toHaveBeenCalledWith(
        facilityTypeCollection,
        ...additionalFacilityTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const parentFacility: IFacility = { id: 4866 };
      facility.parentFacility = parentFacility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [parentFacility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const ownerParty: IParty = { id: 24527 };
      facility.ownerParty = ownerParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [ownerParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityGroup query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const facilityGroup: IFacilityGroup = { id: 19761 };
      facility.facilityGroup = facilityGroup;

      const facilityGroupCollection: IFacilityGroup[] = [{ id: 19761 }];
      vi.spyOn(facilityGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityGroupCollection })));
      const additionalFacilityGroups = [facilityGroup];
      const expectedCollection: IFacilityGroup[] = [...additionalFacilityGroups, ...facilityGroupCollection];
      vi.spyOn(facilityGroupService, 'addFacilityGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(facilityGroupService.query).toHaveBeenCalled();
      expect(facilityGroupService.addFacilityGroupToCollectionIfMissing).toHaveBeenCalledWith(
        facilityGroupCollection,
        ...additionalFacilityGroups.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityGroupsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const sizeUom: IUom = { id: 6576 };
      facility.sizeUom = sizeUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [sizeUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call GeoPoint query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const geoPoint: IGeoPoint = { id: 30719 };
      facility.geoPoint = geoPoint;

      const geoPointCollection: IGeoPoint[] = [{ id: 30719 }];
      vi.spyOn(geoPointService, 'query').mockReturnValue(of(new HttpResponse({ body: geoPointCollection })));
      const additionalGeoPoints = [geoPoint];
      const expectedCollection: IGeoPoint[] = [...additionalGeoPoints, ...geoPointCollection];
      vi.spyOn(geoPointService, 'addGeoPointToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(geoPointService.query).toHaveBeenCalled();
      expect(geoPointService.addGeoPointToCollectionIfMissing).toHaveBeenCalledWith(
        geoPointCollection,
        ...additionalGeoPoints.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geoPointsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InventoryItemType query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      facility.inventoryItemType = inventoryItemType;

      const inventoryItemTypeCollection: IInventoryItemType[] = [{ id: 9002 }];
      vi.spyOn(inventoryItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemTypeCollection })));
      const additionalInventoryItemTypes = [inventoryItemType];
      const expectedCollection: IInventoryItemType[] = [...additionalInventoryItemTypes, ...inventoryItemTypeCollection];
      vi.spyOn(inventoryItemTypeService, 'addInventoryItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(inventoryItemTypeService.query).toHaveBeenCalled();
      expect(inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemTypeCollection,
        ...additionalInventoryItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductStore query and add missing value', () => {
      const facility: IFacility = { id: 6958 };
      const productStore: IProductStore = { id: 30593 };
      facility.productStore = productStore;

      const productStoreCollection: IProductStore[] = [{ id: 30593 }];
      vi.spyOn(productStoreService, 'query').mockReturnValue(of(new HttpResponse({ body: productStoreCollection })));
      const additionalProductStores = [productStore];
      const expectedCollection: IProductStore[] = [...additionalProductStores, ...productStoreCollection];
      vi.spyOn(productStoreService, 'addProductStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(productStoreService.query).toHaveBeenCalled();
      expect(productStoreService.addProductStoreToCollectionIfMissing).toHaveBeenCalledWith(
        productStoreCollection,
        ...additionalProductStores.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productStoresSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facility: IFacility = { id: 6958 };
      const facilityType: IFacilityType = { id: 18255 };
      facility.facilityType = facilityType;
      const parentFacility: IFacility = { id: 4866 };
      facility.parentFacility = parentFacility;
      const ownerParty: IParty = { id: 24527 };
      facility.ownerParty = ownerParty;
      const facilityGroup: IFacilityGroup = { id: 19761 };
      facility.facilityGroup = facilityGroup;
      const sizeUom: IUom = { id: 6576 };
      facility.sizeUom = sizeUom;
      const geoPoint: IGeoPoint = { id: 30719 };
      facility.geoPoint = geoPoint;
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      facility.inventoryItemType = inventoryItemType;
      const productStore: IProductStore = { id: 30593 };
      facility.productStore = productStore;

      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      expect(comp.facilityTypesSharedCollection()).toContainEqual(facilityType);
      expect(comp.facilitiesSharedCollection()).toContainEqual(parentFacility);
      expect(comp.partiesSharedCollection()).toContainEqual(ownerParty);
      expect(comp.facilityGroupsSharedCollection()).toContainEqual(facilityGroup);
      expect(comp.uomsSharedCollection()).toContainEqual(sizeUom);
      expect(comp.geoPointsSharedCollection()).toContainEqual(geoPoint);
      expect(comp.inventoryItemTypesSharedCollection()).toContainEqual(inventoryItemType);
      expect(comp.productStoresSharedCollection()).toContainEqual(productStore);
      expect(comp.facility).toEqual(facility);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacility>();
      const facility = { id: 4866 };
      vi.spyOn(facilityFormService, 'getFacility').mockReturnValue(facility);
      vi.spyOn(facilityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facility);
      saveSubject.complete();

      // THEN
      expect(facilityFormService.getFacility).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityService.update).toHaveBeenCalledWith(expect.objectContaining(facility));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacility>();
      const facility = { id: 4866 };
      vi.spyOn(facilityFormService, 'getFacility').mockReturnValue({ id: null });
      vi.spyOn(facilityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facility: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facility);
      saveSubject.complete();

      // THEN
      expect(facilityFormService.getFacility).toHaveBeenCalled();
      expect(facilityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacility>();
      const facility = { id: 4866 };
      vi.spyOn(facilityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFacilityType', () => {
      it('should forward to facilityTypeService', () => {
        const entity = { id: 18255 };
        const entity2 = { id: 4814 };
        vi.spyOn(facilityTypeService, 'compareFacilityType');
        comp.compareFacilityType(entity, entity2);
        expect(facilityTypeService.compareFacilityType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacilityGroup', () => {
      it('should forward to facilityGroupService', () => {
        const entity = { id: 19761 };
        const entity2 = { id: 10499 };
        vi.spyOn(facilityGroupService, 'compareFacilityGroup');
        comp.compareFacilityGroup(entity, entity2);
        expect(facilityGroupService.compareFacilityGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGeoPoint', () => {
      it('should forward to geoPointService', () => {
        const entity = { id: 30719 };
        const entity2 = { id: 8540 };
        vi.spyOn(geoPointService, 'compareGeoPoint');
        comp.compareGeoPoint(entity, entity2);
        expect(geoPointService.compareGeoPoint).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInventoryItemType', () => {
      it('should forward to inventoryItemTypeService', () => {
        const entity = { id: 9002 };
        const entity2 = { id: 3434 };
        vi.spyOn(inventoryItemTypeService, 'compareInventoryItemType');
        comp.compareInventoryItemType(entity, entity2);
        expect(inventoryItemTypeService.compareInventoryItemType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductStore', () => {
      it('should forward to productStoreService', () => {
        const entity = { id: 30593 };
        const entity2 = { id: 24662 };
        vi.spyOn(productStoreService, 'compareProductStore');
        comp.compareProductStore(entity, entity2);
        expect(productStoreService.compareProductStore).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
