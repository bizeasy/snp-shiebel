import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ShoppingListTypeService } from '../service/shopping-list-type.service';
import { IShoppingListType } from '../shopping-list-type.model';

import { ShoppingListTypeFormService } from './shopping-list-type-form.service';
import { ShoppingListTypeUpdate } from './shopping-list-type-update';

describe('ShoppingListType Management Update Component', () => {
  let comp: ShoppingListTypeUpdate;
  let fixture: ComponentFixture<ShoppingListTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let shoppingListTypeFormService: ShoppingListTypeFormService;
  let shoppingListTypeService: ShoppingListTypeService;

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

    fixture = TestBed.createComponent(ShoppingListTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shoppingListTypeFormService = TestBed.inject(ShoppingListTypeFormService);
    shoppingListTypeService = TestBed.inject(ShoppingListTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const shoppingListType: IShoppingListType = { id: 25325 };

      activatedRoute.data = of({ shoppingListType });
      comp.ngOnInit();

      expect(comp.shoppingListType).toEqual(shoppingListType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListType>();
      const shoppingListType = { id: 6209 };
      vi.spyOn(shoppingListTypeFormService, 'getShoppingListType').mockReturnValue(shoppingListType);
      vi.spyOn(shoppingListTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingListType);
      saveSubject.complete();

      // THEN
      expect(shoppingListTypeFormService.getShoppingListType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shoppingListTypeService.update).toHaveBeenCalledWith(expect.objectContaining(shoppingListType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListType>();
      const shoppingListType = { id: 6209 };
      vi.spyOn(shoppingListTypeFormService, 'getShoppingListType').mockReturnValue({ id: null });
      vi.spyOn(shoppingListTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingListType);
      saveSubject.complete();

      // THEN
      expect(shoppingListTypeFormService.getShoppingListType).toHaveBeenCalled();
      expect(shoppingListTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListType>();
      const shoppingListType = { id: 6209 };
      vi.spyOn(shoppingListTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shoppingListTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
