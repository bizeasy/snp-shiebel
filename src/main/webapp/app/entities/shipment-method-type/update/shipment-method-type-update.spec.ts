import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ShipmentMethodTypeService } from '../service/shipment-method-type.service';
import { IShipmentMethodType } from '../shipment-method-type.model';

import { ShipmentMethodTypeFormService } from './shipment-method-type-form.service';
import { ShipmentMethodTypeUpdate } from './shipment-method-type-update';

describe('ShipmentMethodType Management Update Component', () => {
  let comp: ShipmentMethodTypeUpdate;
  let fixture: ComponentFixture<ShipmentMethodTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentMethodTypeFormService: ShipmentMethodTypeFormService;
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

    fixture = TestBed.createComponent(ShipmentMethodTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentMethodTypeFormService = TestBed.inject(ShipmentMethodTypeFormService);
    shipmentMethodTypeService = TestBed.inject(ShipmentMethodTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const shipmentMethodType: IShipmentMethodType = { id: 25543 };

      activatedRoute.data = of({ shipmentMethodType });
      comp.ngOnInit();

      expect(comp.shipmentMethodType).toEqual(shipmentMethodType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentMethodType>();
      const shipmentMethodType = { id: 6802 };
      vi.spyOn(shipmentMethodTypeFormService, 'getShipmentMethodType').mockReturnValue(shipmentMethodType);
      vi.spyOn(shipmentMethodTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentMethodType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentMethodType);
      saveSubject.complete();

      // THEN
      expect(shipmentMethodTypeFormService.getShipmentMethodType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentMethodTypeService.update).toHaveBeenCalledWith(expect.objectContaining(shipmentMethodType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentMethodType>();
      const shipmentMethodType = { id: 6802 };
      vi.spyOn(shipmentMethodTypeFormService, 'getShipmentMethodType').mockReturnValue({ id: null });
      vi.spyOn(shipmentMethodTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentMethodType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentMethodType);
      saveSubject.complete();

      // THEN
      expect(shipmentMethodTypeFormService.getShipmentMethodType).toHaveBeenCalled();
      expect(shipmentMethodTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentMethodType>();
      const shipmentMethodType = { id: 6802 };
      vi.spyOn(shipmentMethodTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentMethodType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentMethodTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
