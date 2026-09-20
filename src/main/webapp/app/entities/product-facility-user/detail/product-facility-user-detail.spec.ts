import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductFacilityUserDetail } from './product-facility-user-detail';

describe('ProductFacilityUser Management Detail Component', () => {
  let comp: ProductFacilityUserDetail;
  let fixture: ComponentFixture<ProductFacilityUserDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-facility-user-detail').then(m => m.ProductFacilityUserDetail),
              resolve: { productFacilityUser: () => of({ id: 234 }) },
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
    fixture = TestBed.createComponent(ProductFacilityUserDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productFacilityUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductFacilityUserDetail);

      // THEN
      expect(instance.productFacilityUser()).toEqual(expect.objectContaining({ id: 234 }));
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
