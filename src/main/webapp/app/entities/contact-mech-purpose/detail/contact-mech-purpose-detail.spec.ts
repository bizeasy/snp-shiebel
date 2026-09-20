import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ContactMechPurposeDetail } from './contact-mech-purpose-detail';

describe('ContactMechPurpose Management Detail Component', () => {
  let comp: ContactMechPurposeDetail;
  let fixture: ComponentFixture<ContactMechPurposeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./contact-mech-purpose-detail').then(m => m.ContactMechPurposeDetail),
              resolve: { contactMechPurpose: () => of({ id: 25033 }) },
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
    fixture = TestBed.createComponent(ContactMechPurposeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load contactMechPurpose on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContactMechPurposeDetail);

      // THEN
      expect(instance.contactMechPurpose()).toEqual(expect.objectContaining({ id: 25033 }));
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
