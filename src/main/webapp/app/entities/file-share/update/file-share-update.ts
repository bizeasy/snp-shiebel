import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { SharePermission } from 'app/entities/enumerations/share-permission.model';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { FileFolderService } from 'app/entities/file-folder/service/file-folder.service';
import { IParty } from 'app/entities/party/party.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IFileShare } from '../file-share.model';
import { FileShareService } from '../service/file-share.service';

import { FileShareFormGroup, FileShareFormService } from './file-share-form.service';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'sys-file-share-update',
  templateUrl: './file-share-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FileShareUpdate implements OnInit {
  readonly isSaving = signal(false);
  fileShare: IFileShare | null = null;
  sharePermissionValues = Object.keys(SharePermission);

  fileFoldersSharedCollection = signal<IFileFolder[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected fileShareService = inject(FileShareService);
  protected fileShareFormService = inject(FileShareFormService);
  protected fileFolderService = inject(FileFolderService);
  protected attachmentService = inject(AttachmentService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FileShareFormGroup = this.fileShareFormService.createFileShareFormGroup();

  compareFileFolder = (o1: IFileFolder | null, o2: IFileFolder | null): boolean => this.fileFolderService.compareFileFolder(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileShare }) => {
      this.fileShare = fileShare;
      if (fileShare) {
        this.updateForm(fileShare);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const fileShare = this.fileShareFormService.getFileShare(this.editForm);
    if (fileShare.id === null) {
      this.subscribeToSaveResponse(this.fileShareService.create(fileShare));
    } else {
      this.subscribeToSaveResponse(this.fileShareService.update(fileShare));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFileShare | null>): void {
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

  protected updateForm(fileShare: IFileShare): void {
    this.fileShare = fileShare;
    this.fileShareFormService.resetForm(this.editForm, fileShare);

    this.fileFoldersSharedCollection.update(fileFolders =>
      this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, fileShare.folder),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, fileShare.attachment),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, fileShare.sharedWith, fileShare.sharedBy),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fileFolderService
      .query()
      .pipe(map((res: HttpResponse<IFileFolder[]>) => res.body ?? []))
      .pipe(
        map((fileFolders: IFileFolder[]) =>
          this.fileFolderService.addFileFolderToCollectionIfMissing<IFileFolder>(fileFolders, this.fileShare?.folder),
        ),
      )
      .subscribe((fileFolders: IFileFolder[]) => this.fileFoldersSharedCollection.set(fileFolders));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.fileShare?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.fileShare?.sharedWith, this.fileShare?.sharedBy),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
