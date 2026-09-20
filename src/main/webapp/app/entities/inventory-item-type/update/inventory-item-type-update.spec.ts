import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItemType } from '../inventory-item-type.model';
import { InventoryItemTypeService } from '../service/inventory-item-type.service';

import { InventoryItemTypeFormService } from './inventory-item-type-form.service';
import { InventoryItemTypeUpdate } from './inventory-item-type-update';

describe('InventoryItemType Management Update Component', () => {
  let comp: InventoryItemTypeUpdate;
  let fixture: ComponentFixture<InventoryItemTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemTypeFormService: InventoryItemTypeFormService;
  let inventoryItemTypeService: InventoryItemTypeService;

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

    fixture = TestBed.createComponent(InventoryItemTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemTypeFormService = TestBed.inject(InventoryItemTypeFormService);
    inventoryItemTypeService = TestBed.inject(InventoryItemTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const inventoryItemType: IInventoryItemType = { id: 3434 };

      activatedRoute.data = of({ inventoryItemType });
      comp.ngOnInit();

      expect(comp.inventoryItemType).toEqual(inventoryItemType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemType>();
      const inventoryItemType = { id: 9002 };
      vi.spyOn(inventoryItemTypeFormService, 'getInventoryItemType').mockReturnValue(inventoryItemType);
      vi.spyOn(inventoryItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemType);
      saveSubject.complete();

      // THEN
      expect(inventoryItemTypeFormService.getInventoryItemType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemTypeService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItemType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemType>();
      const inventoryItemType = { id: 9002 };
      vi.spyOn(inventoryItemTypeFormService, 'getInventoryItemType').mockReturnValue({ id: null });
      vi.spyOn(inventoryItemTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemType);
      saveSubject.complete();

      // THEN
      expect(inventoryItemTypeFormService.getInventoryItemType).toHaveBeenCalled();
      expect(inventoryItemTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemType>();
      const inventoryItemType = { id: 9002 };
      vi.spyOn(inventoryItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
