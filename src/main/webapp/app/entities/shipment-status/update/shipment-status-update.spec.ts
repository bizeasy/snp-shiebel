import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { ShipmentStatusService } from '../service/shipment-status.service';
import { IShipmentStatus } from '../shipment-status.model';

import { ShipmentStatusFormService } from './shipment-status-form.service';
import { ShipmentStatusUpdate } from './shipment-status-update';

describe('ShipmentStatus Management Update Component', () => {
  let comp: ShipmentStatusUpdate;
  let fixture: ComponentFixture<ShipmentStatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentStatusFormService: ShipmentStatusFormService;
  let shipmentStatusService: ShipmentStatusService;
  let shipmentService: ShipmentService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(ShipmentStatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentStatusFormService = TestBed.inject(ShipmentStatusFormService);
    shipmentStatusService = TestBed.inject(ShipmentStatusService);
    shipmentService = TestBed.inject(ShipmentService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Shipment query and add missing value', () => {
      const shipmentStatus: IShipmentStatus = { id: 18749 };
      const shipment: IShipment = { id: 9594 };
      shipmentStatus.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentStatus });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const shipmentStatus: IShipmentStatus = { id: 18749 };
      const status: IStatus = { id: 2927 };
      shipmentStatus.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentStatus });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shipmentStatus: IShipmentStatus = { id: 18749 };
      const shipment: IShipment = { id: 9594 };
      shipmentStatus.shipment = shipment;
      const status: IStatus = { id: 2927 };
      shipmentStatus.status = status;

      activatedRoute.data = of({ shipmentStatus });
      comp.ngOnInit();

      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.shipmentStatus).toEqual(shipmentStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentStatus>();
      const shipmentStatus = { id: 26844 };
      vi.spyOn(shipmentStatusFormService, 'getShipmentStatus').mockReturnValue(shipmentStatus);
      vi.spyOn(shipmentStatusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentStatus);
      saveSubject.complete();

      // THEN
      expect(shipmentStatusFormService.getShipmentStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentStatusService.update).toHaveBeenCalledWith(expect.objectContaining(shipmentStatus));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentStatus>();
      const shipmentStatus = { id: 26844 };
      vi.spyOn(shipmentStatusFormService, 'getShipmentStatus').mockReturnValue({ id: null });
      vi.spyOn(shipmentStatusService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentStatus);
      saveSubject.complete();

      // THEN
      expect(shipmentStatusFormService.getShipmentStatus).toHaveBeenCalled();
      expect(shipmentStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentStatus>();
      const shipmentStatus = { id: 26844 };
      vi.spyOn(shipmentStatusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShipment', () => {
      it('should forward to shipmentService', () => {
        const entity = { id: 9594 };
        const entity2 = { id: 9190 };
        vi.spyOn(shipmentService, 'compareShipment');
        comp.compareShipment(entity, entity2);
        expect(shipmentService.compareShipment).toHaveBeenCalledWith(entity, entity2);
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
  });
});
