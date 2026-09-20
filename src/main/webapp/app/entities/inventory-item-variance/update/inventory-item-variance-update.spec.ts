import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IInventoryItemVariance } from '../inventory-item-variance.model';
import { InventoryItemVarianceService } from '../service/inventory-item-variance.service';

import { InventoryItemVarianceFormService } from './inventory-item-variance-form.service';
import { InventoryItemVarianceUpdate } from './inventory-item-variance-update';

describe('InventoryItemVariance Management Update Component', () => {
  let comp: InventoryItemVarianceUpdate;
  let fixture: ComponentFixture<InventoryItemVarianceUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemVarianceFormService: InventoryItemVarianceFormService;
  let inventoryItemVarianceService: InventoryItemVarianceService;
  let inventoryItemService: InventoryItemService;

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

    fixture = TestBed.createComponent(InventoryItemVarianceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemVarianceFormService = TestBed.inject(InventoryItemVarianceFormService);
    inventoryItemVarianceService = TestBed.inject(InventoryItemVarianceService);
    inventoryItemService = TestBed.inject(InventoryItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InventoryItem query and add missing value', () => {
      const inventoryItemVariance: IInventoryItemVariance = { id: 6641 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryItemVariance.inventoryItem = inventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [inventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemVariance });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventoryItemVariance: IInventoryItemVariance = { id: 6641 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryItemVariance.inventoryItem = inventoryItem;

      activatedRoute.data = of({ inventoryItemVariance });
      comp.ngOnInit();

      expect(comp.inventoryItemsSharedCollection()).toContainEqual(inventoryItem);
      expect(comp.inventoryItemVariance).toEqual(inventoryItemVariance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemVariance>();
      const inventoryItemVariance = { id: 7138 };
      vi.spyOn(inventoryItemVarianceFormService, 'getInventoryItemVariance').mockReturnValue(inventoryItemVariance);
      vi.spyOn(inventoryItemVarianceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemVariance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemVariance);
      saveSubject.complete();

      // THEN
      expect(inventoryItemVarianceFormService.getInventoryItemVariance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemVarianceService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItemVariance));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemVariance>();
      const inventoryItemVariance = { id: 7138 };
      vi.spyOn(inventoryItemVarianceFormService, 'getInventoryItemVariance').mockReturnValue({ id: null });
      vi.spyOn(inventoryItemVarianceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemVariance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemVariance);
      saveSubject.complete();

      // THEN
      expect(inventoryItemVarianceFormService.getInventoryItemVariance).toHaveBeenCalled();
      expect(inventoryItemVarianceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemVariance>();
      const inventoryItemVariance = { id: 7138 };
      vi.spyOn(inventoryItemVarianceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemVariance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemVarianceService.update).toHaveBeenCalled();
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
  });
});
