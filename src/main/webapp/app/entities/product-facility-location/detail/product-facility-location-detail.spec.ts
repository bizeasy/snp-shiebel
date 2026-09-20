import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductFacilityLocationDetail } from './product-facility-location-detail';

describe('ProductFacilityLocation Management Detail Component', () => {
  let comp: ProductFacilityLocationDetail;
  let fixture: ComponentFixture<ProductFacilityLocationDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-facility-location-detail').then(m => m.ProductFacilityLocationDetail),
              resolve: { productFacilityLocation: () => of({ id: 22461 }) },
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
    fixture = TestBed.createComponent(ProductFacilityLocationDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productFacilityLocation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductFacilityLocationDetail);

      // THEN
      expect(instance.productFacilityLocation()).toEqual(expect.objectContaining({ id: 22461 }));
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
