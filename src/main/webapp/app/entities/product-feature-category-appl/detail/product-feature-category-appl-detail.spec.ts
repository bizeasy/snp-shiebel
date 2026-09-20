import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductFeatureCategoryApplDetail } from './product-feature-category-appl-detail';

describe('ProductFeatureCategoryAppl Management Detail Component', () => {
  let comp: ProductFeatureCategoryApplDetail;
  let fixture: ComponentFixture<ProductFeatureCategoryApplDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-feature-category-appl-detail').then(m => m.ProductFeatureCategoryApplDetail),
              resolve: { productFeatureCategoryAppl: () => of({ id: 5520 }) },
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
    fixture = TestBed.createComponent(ProductFeatureCategoryApplDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productFeatureCategoryAppl on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductFeatureCategoryApplDetail);

      // THEN
      expect(instance.productFeatureCategoryAppl()).toEqual(expect.objectContaining({ id: 5520 }));
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
