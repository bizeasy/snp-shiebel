import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CarrierShipmentMethodDetail } from './carrier-shipment-method-detail';

describe('CarrierShipmentMethod Management Detail Component', () => {
  let comp: CarrierShipmentMethodDetail;
  let fixture: ComponentFixture<CarrierShipmentMethodDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./carrier-shipment-method-detail').then(m => m.CarrierShipmentMethodDetail),
              resolve: { carrierShipmentMethod: () => of({ id: 14904 }) },
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
    fixture = TestBed.createComponent(CarrierShipmentMethodDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load carrierShipmentMethod on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CarrierShipmentMethodDetail);

      // THEN
      expect(instance.carrierShipmentMethod()).toEqual(expect.objectContaining({ id: 14904 }));
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
