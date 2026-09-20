import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TaxAuthorityRateTypeDetail } from './tax-authority-rate-type-detail';

describe('TaxAuthorityRateType Management Detail Component', () => {
  let comp: TaxAuthorityRateTypeDetail;
  let fixture: ComponentFixture<TaxAuthorityRateTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tax-authority-rate-type-detail').then(m => m.TaxAuthorityRateTypeDetail),
              resolve: { taxAuthorityRateType: () => of({ id: 11026 }) },
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
    fixture = TestBed.createComponent(TaxAuthorityRateTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load taxAuthorityRateType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TaxAuthorityRateTypeDetail);

      // THEN
      expect(instance.taxAuthorityRateType()).toEqual(expect.objectContaining({ id: 11026 }));
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
