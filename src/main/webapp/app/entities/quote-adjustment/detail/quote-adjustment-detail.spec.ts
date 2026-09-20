import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { QuoteAdjustmentDetail } from './quote-adjustment-detail';

describe('QuoteAdjustment Management Detail Component', () => {
  let comp: QuoteAdjustmentDetail;
  let fixture: ComponentFixture<QuoteAdjustmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./quote-adjustment-detail').then(m => m.QuoteAdjustmentDetail),
              resolve: { quoteAdjustment: () => of({ id: 2656 }) },
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
    fixture = TestBed.createComponent(QuoteAdjustmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load quoteAdjustment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuoteAdjustmentDetail);

      // THEN
      expect(instance.quoteAdjustment()).toEqual(expect.objectContaining({ id: 2656 }));
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
