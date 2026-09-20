import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ShipmentTypeService } from '../service/shipment-type.service';
import { IShipmentType } from '../shipment-type.model';

import { ShipmentTypeFormService } from './shipment-type-form.service';
import { ShipmentTypeUpdate } from './shipment-type-update';

describe('ShipmentType Management Update Component', () => {
  let comp: ShipmentTypeUpdate;
  let fixture: ComponentFixture<ShipmentTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentTypeFormService: ShipmentTypeFormService;
  let shipmentTypeService: ShipmentTypeService;

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

    fixture = TestBed.createComponent(ShipmentTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentTypeFormService = TestBed.inject(ShipmentTypeFormService);
    shipmentTypeService = TestBed.inject(ShipmentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const shipmentType: IShipmentType = { id: 32676 };

      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      expect(comp.shipmentType).toEqual(shipmentType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentType>();
      const shipmentType = { id: 16845 };
      vi.spyOn(shipmentTypeFormService, 'getShipmentType').mockReturnValue(shipmentType);
      vi.spyOn(shipmentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentType);
      saveSubject.complete();

      // THEN
      expect(shipmentTypeFormService.getShipmentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(shipmentType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentType>();
      const shipmentType = { id: 16845 };
      vi.spyOn(shipmentTypeFormService, 'getShipmentType').mockReturnValue({ id: null });
      vi.spyOn(shipmentTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentType);
      saveSubject.complete();

      // THEN
      expect(shipmentTypeFormService.getShipmentType).toHaveBeenCalled();
      expect(shipmentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentType>();
      const shipmentType = { id: 16845 };
      vi.spyOn(shipmentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
