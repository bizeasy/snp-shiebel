import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ShipmentReceiptDetail } from './shipment-receipt-detail';

describe('ShipmentReceipt Management Detail Component', () => {
  let comp: ShipmentReceiptDetail;
  let fixture: ComponentFixture<ShipmentReceiptDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./shipment-receipt-detail').then(m => m.ShipmentReceiptDetail),
              resolve: { shipmentReceipt: () => of({ id: 7355 }) },
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
    fixture = TestBed.createComponent(ShipmentReceiptDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load shipmentReceipt on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShipmentReceiptDetail);

      // THEN
      expect(instance.shipmentReceipt()).toEqual(expect.objectContaining({ id: 7355 }));
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
