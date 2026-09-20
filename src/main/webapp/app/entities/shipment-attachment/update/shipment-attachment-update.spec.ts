import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentAttachmentService } from '../service/shipment-attachment.service';
import { IShipmentAttachment } from '../shipment-attachment.model';

import { ShipmentAttachmentFormService } from './shipment-attachment-form.service';
import { ShipmentAttachmentUpdate } from './shipment-attachment-update';

describe('ShipmentAttachment Management Update Component', () => {
  let comp: ShipmentAttachmentUpdate;
  let fixture: ComponentFixture<ShipmentAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentAttachmentFormService: ShipmentAttachmentFormService;
  let shipmentAttachmentService: ShipmentAttachmentService;
  let shipmentService: ShipmentService;
  let attachmentService: AttachmentService;

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

    fixture = TestBed.createComponent(ShipmentAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentAttachmentFormService = TestBed.inject(ShipmentAttachmentFormService);
    shipmentAttachmentService = TestBed.inject(ShipmentAttachmentService);
    shipmentService = TestBed.inject(ShipmentService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Shipment query and add missing value', () => {
      const shipmentAttachment: IShipmentAttachment = { id: 12976 };
      const shipment: IShipment = { id: 9594 };
      shipmentAttachment.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentAttachment });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const shipmentAttachment: IShipmentAttachment = { id: 12976 };
      const attachment: IAttachment = { id: 8078 };
      shipmentAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shipmentAttachment: IShipmentAttachment = { id: 12976 };
      const shipment: IShipment = { id: 9594 };
      shipmentAttachment.shipment = shipment;
      const attachment: IAttachment = { id: 8078 };
      shipmentAttachment.attachment = attachment;

      activatedRoute.data = of({ shipmentAttachment });
      comp.ngOnInit();

      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.shipmentAttachment).toEqual(shipmentAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentAttachment>();
      const shipmentAttachment = { id: 126 };
      vi.spyOn(shipmentAttachmentFormService, 'getShipmentAttachment').mockReturnValue(shipmentAttachment);
      vi.spyOn(shipmentAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentAttachment);
      saveSubject.complete();

      // THEN
      expect(shipmentAttachmentFormService.getShipmentAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(shipmentAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentAttachment>();
      const shipmentAttachment = { id: 126 };
      vi.spyOn(shipmentAttachmentFormService, 'getShipmentAttachment').mockReturnValue({ id: null });
      vi.spyOn(shipmentAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentAttachment);
      saveSubject.complete();

      // THEN
      expect(shipmentAttachmentFormService.getShipmentAttachment).toHaveBeenCalled();
      expect(shipmentAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentAttachment>();
      const shipmentAttachment = { id: 126 };
      vi.spyOn(shipmentAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentAttachmentService.update).toHaveBeenCalled();
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

    describe('compareAttachment', () => {
      it('should forward to attachmentService', () => {
        const entity = { id: 8078 };
        const entity2 = { id: 16801 };
        vi.spyOn(attachmentService, 'compareAttachment');
        comp.compareAttachment(entity, entity2);
        expect(attachmentService.compareAttachment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
