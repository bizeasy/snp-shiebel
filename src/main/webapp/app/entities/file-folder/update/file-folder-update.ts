import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFileFolder } from '../file-folder.model';
import { FileFolderService } from '../service/file-folder.service';

import { FileFolderFormGroup, FileFolderFormService } from './file-folder-form.service';

@Component({
  selector: 'sys-file-folder-update',
  templateUrl: './file-folder-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FileFolderUpdate implements OnInit {
  readonly isSaving = signal(false);
  fileFolder: IFileFolder | null = null;

  fileFoldersSharedCollection = signal<IFileFolder[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected fileFolderService = inject(FileFolderService);
  protected fileFolderFormService = inject(FileFolderFormService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FileFolderFormGroup = this.fileFolderFormService.createFileFolderFormGroup();

  compareFileFolder = (o1: IFileFolder | null, o2: IFileFolder | null): boolean => this.fileFolderService.compareFileFolder(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileFolder }) => {
      this.fileFolder = fileFolder;
      if (fileFolder) {
        this.updateForm(fileFolder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const fileFolder = this.fileFolderFormService.getFileFolder(this.editForm);
    if (fileFolder.id === null) {
      this.subscribeToSaveResponse(this.fileFolderService.create(fileFolder));
    } else {
      this.subscribeToSaveResponse(this.fileFolderService.update(fileFolder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFileFolder | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(fileFolder: IFileFolder): void {
    this.fileFolder = fileFolder;
    this.fileFolderFormService.resetForm(this.editForm, fileFolder);

    this.fileFoldersSharedCollection.update(fileFolders =>
      this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, fileFolder.parent),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, fileFolder.owner));
  }

  protected loadRelationshipsOptions(): void {
    this.fileFolderService
      .query()
      .pipe(map((res: HttpResponse<IFileFolder[]>) => res.body ?? []))
      .pipe(
        map((fileFolders: IFileFolder[]) =>
          this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, this.fileFolder?.parent),
        ),
      )
      .subscribe((fileFolders: IFileFolder[]) => this.fileFoldersSharedCollection.set(fileFolders));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.fileFolder?.owner)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
