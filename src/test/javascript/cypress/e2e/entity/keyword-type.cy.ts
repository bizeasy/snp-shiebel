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

describe('KeywordType e2e test', () => {
  const keywordTypePageUrl = '/keyword-type';
  let username: string;
  let password: string;
  const keywordTypeSample = { name: 'humor uh-huh eek' };

  let keywordType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/keyword-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/keyword-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/keyword-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (keywordType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/keyword-types/${keywordType.id}`,
      }).then(() => {
        keywordType = undefined;
      });
    }
  });

  it('KeywordTypes menu should load KeywordTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('keyword-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('KeywordType').should('exist');
    cy.location('pathname').should('eq', keywordTypePageUrl);
  });

  describe('KeywordType page', () => {
    it('should have translated page title', () => {
      cy.visit(keywordTypePageUrl);
      cy.getEntityHeading('KeywordType').should('not.contain', 'snpApp.keywordType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(keywordTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create KeywordType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${keywordTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('KeywordType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', keywordTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/keyword-types',
          body: keywordTypeSample,
        }).then(({ body }) => {
          keywordType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/keyword-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [keywordType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(keywordTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details KeywordType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('keywordType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', keywordTypePageUrl);
      });

      it('edit button click should load edit KeywordType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KeywordType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', keywordTypePageUrl);
      });

      it('edit button click should load edit KeywordType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KeywordType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', keywordTypePageUrl);
      });

      it('last delete button click should delete instance of KeywordType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('keywordType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', keywordTypePageUrl);

        keywordType = undefined;
      });
    });
  });

  describe('new KeywordType page', () => {
    beforeEach(() => {
      cy.visit(keywordTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('KeywordType');
    });

    it('should create an instance of KeywordType', () => {
      cy.get(`[data-cy="name"]`).type('stingy');
      cy.get(`[data-cy="name"]`).should('have.value', 'stingy');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        keywordType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', keywordTypePageUrl);
    });
  });
});
