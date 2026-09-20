import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FacilityCarrierShipmentDetail } from './facility-carrier-shipment-detail';

describe('FacilityCarrierShipment Management Detail Component', () => {
  let comp: FacilityCarrierShipmentDetail;
  let fixture: ComponentFixture<FacilityCarrierShipmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./facility-carrier-shipment-detail').then(m => m.FacilityCarrierShipmentDetail),
              resolve: { facilityCarrierShipment: () => of({ id: 12764 }) },
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
    fixture = TestBed.createComponent(FacilityCarrierShipmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load facilityCarrierShipment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FacilityCarrierShipmentDetail);

      // THEN
      expect(instance.facilityCarrierShipment()).toEqual(expect.objectContaining({ id: 12764 }));
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
