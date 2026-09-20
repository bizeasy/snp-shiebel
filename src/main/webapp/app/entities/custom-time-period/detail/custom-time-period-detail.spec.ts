import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CustomTimePeriodDetail } from './custom-time-period-detail';

describe('CustomTimePeriod Management Detail Component', () => {
  let comp: CustomTimePeriodDetail;
  let fixture: ComponentFixture<CustomTimePeriodDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./custom-time-period-detail').then(m => m.CustomTimePeriodDetail),
              resolve: { customTimePeriod: () => of({ id: 19478 }) },
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
    fixture = TestBed.createComponent(CustomTimePeriodDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load customTimePeriod on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustomTimePeriodDetail);

      // THEN
      expect(instance.customTimePeriod()).toEqual(expect.objectContaining({ id: 19478 }));
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
