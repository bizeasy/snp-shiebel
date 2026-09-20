import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('FileFolder e2e test', () => {
  const fileFolderPageUrl = '/file-folder';
  let username: string;
  let password: string;
  const fileFolderSample = { name: 'fiercely seemingly' };

  let fileFolder;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/file-folders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/file-folders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/file-folders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fileFolder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/file-folders/${fileFolder.id}`,
      }).then(() => {
        fileFolder = undefined;
      });
    }
  });

  it('FileFolders menu should load FileFolders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-folder');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FileFolder').should('exist');
    cy.location('pathname').should('eq', fileFolderPageUrl);
  });

  describe('FileFolder page', () => {
    it('should have translated page title', () => {
      cy.visit(fileFolderPageUrl);
      cy.getEntityHeading('FileFolder').should('not.contain', 'snpApp.fileFolder.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fileFolderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FileFolder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fileFolderPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FileFolder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileFolderPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/file-folders',
          body: fileFolderSample,
        }).then(({ body }) => {
          fileFolder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/file-folders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/file-folders?page=0&size=20>; rel="last",<http://localhost/api/file-folders?page=0&size=20>; rel="first"',
              },
              body: [fileFolder],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fileFolderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FileFolder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fileFolder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileFolderPageUrl);
      });

      it('edit button click should load edit FileFolder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileFolder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileFolderPageUrl);
      });

      it('edit button click should load edit FileFolder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileFolder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileFolderPageUrl);
      });

      it('last delete button click should delete instance of FileFolder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fileFolder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fileFolderPageUrl);

        fileFolder = undefined;
      });
    });
  });

  describe('new FileFolder page', () => {
    beforeEach(() => {
      cy.visit(fileFolderPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FileFolder');
    });

    it('should create an instance of FileFolder', () => {
      cy.get(`[data-cy="name"]`).type('that than');
      cy.get(`[data-cy="name"]`).should('have.value', 'that than');

      cy.get(`[data-cy="path"]`).type('calculus whose alongside');
      cy.get(`[data-cy="path"]`).should('have.value', 'calculus whose alongside');

      cy.get(`[data-cy="isSystemFolder"]`).should('not.be.checked');
      cy.get(`[data-cy="isSystemFolder"]`).click();
      cy.get(`[data-cy="isSystemFolder"]`).should('be.checked');

      cy.get(`[data-cy="isTrashed"]`).should('not.be.checked');
      cy.get(`[data-cy="isTrashed"]`).click();
      cy.get(`[data-cy="isTrashed"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2023-12-14T14:58');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2023-12-14T14:58');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fileFolder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fileFolderPageUrl);
    });
  });
});
