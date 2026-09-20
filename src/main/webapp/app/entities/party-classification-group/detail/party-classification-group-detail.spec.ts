import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartyClassificationGroupDetail } from './party-classification-group-detail';

describe('PartyClassificationGroup Management Detail Component', () => {
  let comp: PartyClassificationGroupDetail;
  let fixture: ComponentFixture<PartyClassificationGroupDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./party-classification-group-detail').then(m => m.PartyClassificationGroupDetail),
              resolve: { partyClassificationGroup: () => of({ id: 8459 }) },
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
    fixture = TestBed.createComponent(PartyClassificationGroupDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partyClassificationGroup on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartyClassificationGroupDetail);

      // THEN
      expect(instance.partyClassificationGroup()).toEqual(expect.objectContaining({ id: 8459 }));
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
