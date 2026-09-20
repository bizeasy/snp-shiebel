import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FacilityGroupTypeDetail } from './facility-group-type-detail';

describe('FacilityGroupType Management Detail Component', () => {
  let comp: FacilityGroupTypeDetail;
  let fixture: ComponentFixture<FacilityGroupTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./facility-group-type-detail').then(m => m.FacilityGroupTypeDetail),
              resolve: { facilityGroupType: () => of({ id: 7510 }) },
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
    fixture = TestBed.createComponent(FacilityGroupTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load facilityGroupType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FacilityGroupTypeDetail);

      // THEN
      expect(instance.facilityGroupType()).toEqual(expect.objectContaining({ id: 7510 }));
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
