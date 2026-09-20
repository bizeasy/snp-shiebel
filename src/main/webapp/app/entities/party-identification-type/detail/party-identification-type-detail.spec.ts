import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartyIdentificationTypeDetail } from './party-identification-type-detail';

describe('PartyIdentificationType Management Detail Component', () => {
  let comp: PartyIdentificationTypeDetail;
  let fixture: ComponentFixture<PartyIdentificationTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./party-identification-type-detail').then(m => m.PartyIdentificationTypeDetail),
              resolve: { partyIdentificationType: () => of({ id: 7941 }) },
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
    fixture = TestBed.createComponent(PartyIdentificationTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partyIdentificationType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartyIdentificationTypeDetail);

      // THEN
      expect(instance.partyIdentificationType()).toEqual(expect.objectContaining({ id: 7941 }));
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
