import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ShipmentAttachmentDetail } from './shipment-attachment-detail';

describe('ShipmentAttachment Management Detail Component', () => {
  let comp: ShipmentAttachmentDetail;
  let fixture: ComponentFixture<ShipmentAttachmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./shipment-attachment-detail').then(m => m.ShipmentAttachmentDetail),
              resolve: { shipmentAttachment: () => of({ id: 126 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShipmentAttachmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load shipmentAttachment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShipmentAttachmentDetail);

      // THEN
      expect(instance.shipmentAttachment()).toEqual(expect.objectContaining({ id: 126 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
