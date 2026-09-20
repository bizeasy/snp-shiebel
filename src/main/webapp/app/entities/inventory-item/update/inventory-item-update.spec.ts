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
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { ILot } from 'app/entities/lot/lot.model';
import { LotService } from 'app/entities/lot/service/lot.service';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { PartyService } from 'app/entities/party/service/party.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';

import { InventoryItemFormService } from './inventory-item-form.service';
import { InventoryItemUpdate } from './inventory-item-update';

describe('InventoryItem Management Update Component', () => {
  let comp: InventoryItemUpdate;
  let fixture: ComponentFixture<InventoryItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemFormService: InventoryItemFormService;
  let inventoryItemService: InventoryItemService;
  let inventoryItemTypeService: InventoryItemTypeService;
  let productService: ProductService;
  let partyService: PartyService;
  let statusService: StatusService;
  let facilityService: FacilityService;
  let facilityLocationService: FacilityLocationService;
  let uomService: UomService;
  let lotService: LotService;

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

    fixture = TestBed.createComponent(InventoryItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemFormService = TestBed.inject(InventoryItemFormService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    inventoryItemTypeService = TestBed.inject(InventoryItemTypeService);
    productService = TestBed.inject(ProductService);
    partyService = TestBed.inject(PartyService);
    statusService = TestBed.inject(StatusService);
    facilityService = TestBed.inject(FacilityService);
    facilityLocationService = TestBed.inject(FacilityLocationService);
    uomService = TestBed.inject(UomService);
    lotService = TestBed.inject(LotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InventoryItemType query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      inventoryItem.inventoryItemType = inventoryItemType;

      const inventoryItemTypeCollection: IInventoryItemType[] = [{ id: 9002 }];
      vi.spyOn(inventoryItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemTypeCollection })));
      const additionalInventoryItemTypes = [inventoryItemType];
      const expectedCollection: IInventoryItemType[] = [...additionalInventoryItemTypes, ...inventoryItemTypeCollection];
      vi.spyOn(inventoryItemTypeService, 'addInventoryItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(inventoryItemTypeService.query).toHaveBeenCalled();
      expect(inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemTypeCollection,
        ...additionalInventoryItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const product: IProduct = { id: 21536 };
      inventoryItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const supplier: IParty = { id: 24527 };
      inventoryItem.supplier = supplier;
      const ownerParty: IParty = { id: 24527 };
      inventoryItem.ownerParty = ownerParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [supplier, ownerParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const status: IStatus = { id: 2927 };
      inventoryItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const facility: IFacility = { id: 4866 };
      inventoryItem.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityLocation query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const facilityLocation: IFacilityLocation = { id: 76 };
      inventoryItem.facilityLocation = facilityLocation;

      const facilityLocationCollection: IFacilityLocation[] = [{ id: 76 }];
      vi.spyOn(facilityLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityLocationCollection })));
      const additionalFacilityLocations = [facilityLocation];
      const expectedCollection: IFacilityLocation[] = [...additionalFacilityLocations, ...facilityLocationCollection];
      vi.spyOn(facilityLocationService, 'addFacilityLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(facilityLocationService.query).toHaveBeenCalled();
      expect(facilityLocationService.addFacilityLocationToCollectionIfMissing).toHaveBeenCalledWith(
        facilityLocationCollection,
        ...additionalFacilityLocations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityLocationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const uom: IUom = { id: 6576 };
      inventoryItem.uom = uom;
      const currencyUom: IUom = { id: 6576 };
      inventoryItem.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom, currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Lot query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const lot: ILot = { id: 16504 };
      inventoryItem.lot = lot;

      const lotCollection: ILot[] = [{ id: 16504 }];
      vi.spyOn(lotService, 'query').mockReturnValue(of(new HttpResponse({ body: lotCollection })));
      const additionalLots = [lot];
      const expectedCollection: ILot[] = [...additionalLots, ...lotCollection];
      vi.spyOn(lotService, 'addLotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(lotService.query).toHaveBeenCalled();
      expect(lotService.addLotToCollectionIfMissing).toHaveBeenCalledWith(
        lotCollection,
        ...additionalLots.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.lotsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      inventoryItem.inventoryItemType = inventoryItemType;
      const product: IProduct = { id: 21536 };
      inventoryItem.product = product;
      const supplier: IParty = { id: 24527 };
      inventoryItem.supplier = supplier;
      const ownerParty: IParty = { id: 24527 };
      inventoryItem.ownerParty = ownerParty;
      const status: IStatus = { id: 2927 };
      inventoryItem.status = status;
      const facility: IFacility = { id: 4866 };
      inventoryItem.facility = facility;
      const facilityLocation: IFacilityLocation = { id: 76 };
      inventoryItem.facilityLocation = facilityLocation;
      const uom: IUom = { id: 6576 };
      inventoryItem.uom = uom;
      const currencyUom: IUom = { id: 6576 };
      inventoryItem.currencyUom = currencyUom;
      const lot: ILot = { id: 16504 };
      inventoryItem.lot = lot;

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(comp.inventoryItemTypesSharedCollection()).toContainEqual(inventoryItemType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.partiesSharedCollection()).toContainEqual(supplier);
      expect(comp.partiesSharedCollection()).toContainEqual(ownerParty);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.facilityLocationsSharedCollection()).toContainEqual(facilityLocation);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.lotsSharedCollection()).toContainEqual(lot);
      expect(comp.inventoryItem).toEqual(inventoryItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItem>();
      const inventoryItem = { id: 7462 };
      vi.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue(inventoryItem);
      vi.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItem);
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItem>();
      const inventoryItem = { id: 7462 };
      vi.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue({ id: null });
      vi.spyOn(inventoryItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItem);
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(inventoryItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItem>();
      const inventoryItem = { id: 7462 };
      vi.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInventoryItemType', () => {
      it('should forward to inventoryItemTypeService', () => {
        const entity = { id: 9002 };
        const entity2 = { id: 3434 };
        vi.spyOn(inventoryItemTypeService, 'compareInventoryItemType');
        comp.compareInventoryItemType(entity, entity2);
        expect(inventoryItemTypeService.compareInventoryItemType).toHaveBeenCalledWith(entity, entity2);
      });
    });

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

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLot', () => {
      it('should forward to lotService', () => {
        const entity = { id: 16504 };
        const entity2 = { id: 5166 };
        vi.spyOn(lotService, 'compareLot');
        comp.compareLot(entity, entity2);
        expect(lotService.compareLot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
