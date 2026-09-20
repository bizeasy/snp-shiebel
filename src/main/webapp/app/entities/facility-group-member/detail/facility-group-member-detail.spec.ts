import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FacilityGroupMemberDetail } from './facility-group-member-detail';

describe('FacilityGroupMember Management Detail Component', () => {
  let comp: FacilityGroupMemberDetail;
  let fixture: ComponentFixture<FacilityGroupMemberDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./facility-group-member-detail').then(m => m.FacilityGroupMemberDetail),
              resolve: { facilityGroupMember: () => of({ id: 26141 }) },
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
    fixture = TestBed.createComponent(FacilityGroupMemberDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load facilityGroupMember on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FacilityGroupMemberDetail);

      // THEN
      expect(instance.facilityGroupMember()).toEqual(expect.objectContaining({ id: 26141 }));
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
