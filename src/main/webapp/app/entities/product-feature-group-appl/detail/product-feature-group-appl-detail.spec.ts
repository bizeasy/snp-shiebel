import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductFeatureGroupApplDetail } from './product-feature-group-appl-detail';

describe('ProductFeatureGroupAppl Management Detail Component', () => {
  let comp: ProductFeatureGroupApplDetail;
  let fixture: ComponentFixture<ProductFeatureGroupApplDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-feature-group-appl-detail').then(m => m.ProductFeatureGroupApplDetail),
              resolve: { productFeatureGroupAppl: () => of({ id: 8696 }) },
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
    fixture = TestBed.createComponent(ProductFeatureGroupApplDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productFeatureGroupAppl on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductFeatureGroupApplDetail);

      // THEN
      expect(instance.productFeatureGroupAppl()).toEqual(expect.objectContaining({ id: 8696 }));
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
