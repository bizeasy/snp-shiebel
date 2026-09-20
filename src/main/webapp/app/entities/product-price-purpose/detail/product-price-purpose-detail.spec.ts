import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ProductPricePurposeDetail } from './product-price-purpose-detail';

describe('ProductPricePurpose Management Detail Component', () => {
  let comp: ProductPricePurposeDetail;
  let fixture: ComponentFixture<ProductPricePurposeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./product-price-purpose-detail').then(m => m.ProductPricePurposeDetail),
              resolve: { productPricePurpose: () => of({ id: 8537 }) },
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
    fixture = TestBed.createComponent(ProductPricePurposeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load productPricePurpose on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductPricePurposeDetail);

      // THEN
      expect(instance.productPricePurpose()).toEqual(expect.objectContaining({ id: 8537 }));
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
