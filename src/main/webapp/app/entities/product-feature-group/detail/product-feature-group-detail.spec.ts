import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductFeatureGroupDetail } from './product-feature-group-detail';

describe('ProductFeatureGroup Management Detail Component', () => {
  let comp: ProductFeatureGroupDetail;
  let fixture: ComponentFixture<ProductFeatureGroupDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-feature-group-detail').then(m => m.ProductFeatureGroupDetail),
              resolve: { productFeatureGroup: () => of({ id: 11544 }) },
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
    fixture = TestBed.createComponent(ProductFeatureGroupDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productFeatureGroup on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductFeatureGroupDetail);

      // THEN
      expect(instance.productFeatureGroup()).toEqual(expect.objectContaining({ id: 11544 }));
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
